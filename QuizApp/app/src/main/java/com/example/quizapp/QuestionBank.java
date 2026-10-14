package com.example.quizapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions() {
        List<Question> list = new ArrayList<>();

        // Add 10+ trivia questions
        list.add(new Question("What is the capital of France?",
                new String[]{"London", "Berlin", "Paris", "Madrid"}, 2));

        list.add(new Question("Which planet is known as the Red Planet?",
                new String[]{"Venus", "Mars", "Jupiter", "Saturn"}, 1));

        list.add(new Question("What is the largest ocean on Earth?",
                new String[]{"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"}, 3));

        list.add(new Question("Who wrote 'Romeo and Juliet'?",
                new String[]{"Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"}, 1));

        list.add(new Question("What is the chemical symbol for gold?",
                new String[]{"Ag", "Fe", "Au", "Pb"}, 2));

        list.add(new Question("Which country hosted the 2016 Summer Olympics?",
                new String[]{"China", "Brazil", "UK", "Japan"}, 1));

        list.add(new Question("What is the powerhouse of the cell?",
                new String[]{"Nucleus", "Ribosome", "Mitochondria", "Endoplasmic Reticulum"}, 2));

        list.add(new Question("In which year did the Titanic sink?",
                new String[]{"1912", "1905", "1920", "1898"}, 0));

        list.add(new Question("What is the fastest land animal?",
                new String[]{"Lion", "Cheetah", "Horse", "Leopard"}, 1));

        list.add(new Question("How many continents are there on Earth?",
                new String[]{"5", "6", "7", "8"}, 2));

        // Shuffle the question order randomly using Collections.shuffle()
        Collections.shuffle(list);

        return list;
    }
}