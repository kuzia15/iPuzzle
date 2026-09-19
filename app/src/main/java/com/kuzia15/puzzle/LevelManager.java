package com.kuzia15.puzzle;

import java.util.ArrayList;
import java.util.Collections;

public class LevelManager {

    public static ArrayList<Level> createLevels() {
        ArrayList<Level> levels = new ArrayList<>();

        levels.add(new Level(
                R.drawable.monitor,
                "Монитор",
                "Монитор показывает то, что делает компьютер.",
                "Монитор", "Камера", "Принтер"));

        levels.add(new Level(
                R.drawable.klava,
                "Клавиатура",
                "На клавиатуре набирают буквы и цифры.",
                "Клавиатура", "Мышь", "Колонка"));

        levels.add(new Level(
                R.drawable.mouse,
                "Мышь",
                "Мышкой двигают курсор по экрану.",
                "Монитор", "Мышь", "Микрофон"));

        levels.add(new Level(
                R.drawable.camera,
                "Камера",
                "Камера снимает видео и фото.",
                "Камера", "Наушники", "Флешка"));

        levels.add(new Level(
                R.drawable.micro,
                "Микрофон",
                "Микрофон записывает голос.",
                "Микрофон", "Принтер", "Монитор"));

        levels.add(new Level(
                R.drawable.headphone,
                "Наушники",
                "В наушниках слушают музыку.",
                "Наушники", "Колонки", "Клавиатура"));

        levels.add(new Level(
                R.drawable.printer,
                "Принтер",
                "Принтер печатает картинки и текст на бумаге.",
                "Принтер", "Телефон", "Камера"));

        levels.add(new Level(
                R.drawable.colonki,
                "Колонка",
                "Колонка играет музыку громко, для всех в кабинете.",
                "Колонка", "Мышь", "Сканер"));

        levels.add(new Level(
                R.drawable.fleshka,
                "Флешка",
                "На флешке хранят файлы и носят её с собой.",
                "Флешка", "Ноутбук", "Микрофон"));

        levels.add(new Level(
                R.drawable.laptop,
                "Ноутбук",
                "Ноутбук - маленький компьютер, который можно носить с собой.",
                "Ноутбук", "Монитор", "Принтер"));

        levels.add(new Level(
                R.drawable.scaner,
                "Сканер",
                "Сканер переносит бумажный документ или фото в компьютер.",
                "Сканер", "Принтер", "Монитор"));

        levels.add(new Level(
                R.drawable.router,
                "Роутер",
                "Роутер раздаёт интернет по Wi-Fi всем устройствам дома.",
                "Роутер", "Модем", "Колонки"));

        levels.add(new Level(
                R.drawable.phone,
                "Телефон",
                "По телефону звонят и пишут сообщения.",
                "Телефон", "Планшет", "Камера"));

        levels.add(new Level(
                R.drawable.tablet,
                "Планшет",
                "Планшет - это большой телефон с сенсорным экраном.",
                "Планшет", "Ноутбук", "Монитор"));

        levels.add(new Level(
                R.drawable.vr,
                "VR-очки",
                "VR-очки помогают попасть в виртуальный мир.",
                "VR-очки", "Телефон", "Флешка"));

        levels.add(new Level(
                R.drawable.joystick,
                "Джойстик",
                "Джойстиком управляют в компьютерных играх.",
                "Джойстик", "Мышь", "Клавиатура"));

        levels.add(new Level(
                R.drawable.projector,
                "Проектор",
                "Проектор показывает картинку на большом экране или стене.",
                "Проектор", "Монитор", "Телевизор"));

        levels.add(new Level(
                R.drawable.videocard,
                "Видеокарта",
                "Видеокарта отвечает за картинку и игры на компьютере.",
                "Видеокарта", "Процессор", "Флешка"));

        levels.add(new Level(
                R.drawable.processor,
                "Процессор",
                "Процессор - это «мозг» компьютера, он всё считает.",
                "Процессор", "Видеокарта", "Мышь"));

        levels.add(new Level(
                R.drawable.powerbank,
                "Повербанк",
                "Повербанк заряжает телефон, когда рядом нет розетки.",
                "Повербанк", "Флешка", "Наушники"));

        Collections.shuffle(levels);
        return levels;
    }

    public static ArrayList<Level> pickLevels(int count) {
        ArrayList<Level> all = createLevels();
        if (count >= all.size()) return all;
        return new ArrayList<>(all.subList(0, count));
    }
}