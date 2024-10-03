package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader reader = new FileReader("src/main/resources/books.json");
        Type visitorListType = new TypeToken<List<Visitor>>() {}.getType();
        List<Visitor> visitors = gson.fromJson(reader, visitorListType);

        // 1 Список посетителей и их количество
        System.out.println("Задание 1: Список посетителей и их количество");
        visitors.forEach(v -> System.out.println(v.getName() + " " + v.getSurname()));
        System.out.println("Количество посетителей: " + visitors.size());

        // 2 Список и количество книг, добавленных в избранное, без повторений
        System.out.println("\nЗадание 2: Список книг в избранном без повторений");
        Set<Book> uniqueBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .collect(Collectors.toSet());
        uniqueBooks.forEach(book -> System.out.println(book.getName()));
        System.out.println("Количество уникальных книг: " + uniqueBooks.size());

        // 3 Отсортировать книги по году издания и вывести список
        System.out.println("\nЗадание 3: Список книг, отсортированный по году издания");
        uniqueBooks.stream()
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .forEach(book -> System.out.println(book.getName() + " (" + book.getPublishingYear() + ")"));

        // 4 Проверить, есть ли у кого-то в избранном книга автора "Jane Austen"
        boolean hasJaneAusten = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .anyMatch(book -> book.getAuthor().equalsIgnoreCase("Jane Austen"));
        System.out.println("\nЗадание 4: Есть ли книга автора 'Jane Austen': " + (hasJaneAusten ? "Да" : "Нет"));

        // 5 Максимальное число добавленных книг в избранное
        int maxBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .max()
                .orElse(0);
        System.out.println("\nЗадание 5: Максимальное количество избранных книг у одного посетителя: " + maxBooks);

        // 6 Создать SMS-сообщения и сгруппировать посетителей
        System.out.println("\nЗадание 6: SMS-сообщения для подписчиков");

        double avgBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .average()
                .orElse(0);

        List<SmsMessage> smsMessages = visitors.stream()
                .filter(Visitor::isSubscribed)
                .map(visitor -> {
                    String message;
                    int bookCount = visitor.getFavoriteBooks().size();
                    if (bookCount > avgBooks) {
                        message = "you are a bookworm";
                    } else if (bookCount < avgBooks) {
                        message = "read more";
                    } else {
                        message = "fine";
                    }
                    return new SmsMessage(visitor.getPhone(), message);
                })
                .collect(Collectors.toList());

        smsMessages.forEach(sms -> System.out.println("To: " + sms.getPhoneNumber() + " - " + sms.getMessage()));
    }
}