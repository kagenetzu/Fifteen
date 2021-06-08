package com.example.fifteen;

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {

    public  int[][] array = new int[4][4];
    public NonePoint none = new NonePoint();

    public void generateArray() {
        int k = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (k >= 16) {
                    none.x = i;
                    none.y = j;
                    array[i][j] = -1;
                } else {
                    array[i][j] = k;
                }
                k++;
            }
        }
    }

    public boolean hasSolution(ArrayList<Integer> list) {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            int current = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                if ((list.get(j) != 0) && (current > list.get(j))) {
                    sum++;
                }
            }
        }
        return sum % 2 == 0;
    }

    public ArrayList<Integer> generateRandomNumbers() {
        ArrayList<Integer> randNumb;
        do {
            randNumb = new ArrayList<>();
            while (randNumb.size() < 15) {
                Integer n = new Random(System.currentTimeMillis()).nextInt(15) + 1;
                if (!randNumb.contains(n)) {
                    randNumb.add(n);
                }
            }
        } while (!hasSolution(randNumb));
        return randNumb;
    }

    public boolean isOver( int[][] ar) {
        try {


            return(ar[0][0] == array[0][0] &&
                    ar[0][1] == array[0][1] &&
                    ar[0][2] == array[0][2] &&
                    ar[0][3] == array[0][3] &&
                    ar[1][0] == array[1][0] &&
                    ar[1][1] == array[1][1] &&
                    ar[1][2] == array[1][2] &&
                    ar[1][3] == array[1][3] &&
                    ar[2][0] == array[2][0] &&
                    ar[2][1] == array[2][1] &&
                    ar[2][2] == array[2][2] &&
                    ar[2][3] == array[2][3] &&
                    ar[3][0] == array[3][0] &&
                    ar[3][1] == array[3][1] &&
                    ar[3][2] == array[3][2]);

        } catch (Exception e) {
            return false;
        }
    }
    public boolean canMove(NonePoint clicked) {
        if (clicked.equals(none)) {
            return false;
        }
        if (clicked.x == none.x) {
            int diff = Math.abs(clicked.y - none.y);
            if (diff == 1) {
                return true;
            }
        } else if (clicked.y == none.y) {
            int diff = Math.abs(clicked.x - none.x);
            if (diff == 1) {
                return true;
            }
        }
        return false;
    }






}
