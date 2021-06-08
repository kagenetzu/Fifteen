package com.example.fifteen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_REQUEST = 1;
    private int k = 0;
    private boolean flag = true;
    private TextView message;
    private Button reset;
    private Button photo;
    private Button spoiler;
    private Button solution;
    private Button backNumber;
    private int[][] check = new int[4][4];
    private GameLogic game = new GameLogic();
    private Bitmap scaledBitmap;
    private Bitmap[] imgs = new Bitmap[16];
    private ImageButton[][] buttons = new ImageButton[4][4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        game.generateArray();

        paintGame();

        setListeners();

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame(flag);
            }
        });
        spoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(MainActivity.this);
                image.setBackgroundColor(getResources().getColor(R.color.button));
                image.setImageBitmap(scaledBitmap);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setView(image).create();
                dialog.show();

            }
        });
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solution(flag);
                showEnd();

            }
        });
        backNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNumber.setVisibility(View.INVISIBLE);
                spoiler.setVisibility(View.INVISIBLE);
                flag = true;
                resetGame(flag);
                message.setText("");
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        switch (requestCode) {

            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    splitBitmap(bitmap);
                    for (int i = 1; i < 16 ; i++) {
                        for (int j = 1; j < 16; j++) {

                        if (getDominantColor(imgs[i]) == getDominantColor(imgs[j])) {
                            k++;
                        }
                        }

                    }
                    if (k > 100) {
                       // System.out.println("no");
                        message.setText(R.string.bad);
                        message.setTextColor(Color.RED);
                    } else {
                        message.setText(R.string.good); //System.out.println("yes");
                        message.setTextColor(Color.GREEN);
                    }
                 //   System.out.println(k);
                    k = 0;
                }
        }
    }

    @Override
    public void onClick(View v) {
        ImageButton clickedButton = (ImageButton) v;
        NonePoint clickedFree = getClickedPoint(clickedButton);
        if (clickedFree != null && game.canMove(clickedFree)) {
            clickedButton.setVisibility(View.INVISIBLE);
            Drawable num = clickedButton.getBackground();
            clickedButton.setBackground(null);
            ImageButton button = buttons[game.none.x][game.none.y];
            button.setVisibility(View.VISIBLE);
            button.setBackground(num);
            check[game.none.x][game.none.y] = check[clickedFree.x][clickedFree.y];
            game.none.x = clickedFree.x;
            game.none.y = clickedFree.y;
            if (game.isOver(check)) {
                System.out.println("Победа");
                showEnd();
            }

        }

    }

    private static int getDominantColor(Bitmap bitmap) {
        final int[] color = new int[1];
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                color[0] = swatch2.getPopulation() - swatch1.getPopulation();
                return color[0];
            }
        });
        return color[0];
    }



    private void showEnd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton("Не начинать снова.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder
                .setMessage("Поздравляем!")
                .setNegativeButton("Начать снова.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resetGame(flag);
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void init() {
        buttons[0][0] = (ImageButton) findViewById(R.id.button1);
        buttons[0][1] = (ImageButton) findViewById(R.id.button2);
        buttons[0][2] = (ImageButton) findViewById(R.id.button3);
        buttons[0][3] = (ImageButton) findViewById(R.id.button4);

        buttons[1][0] = (ImageButton) findViewById(R.id.button5);
        buttons[1][1] = (ImageButton) findViewById(R.id.button6);
        buttons[1][2] = (ImageButton) findViewById(R.id.button7);
        buttons[1][3] = (ImageButton) findViewById(R.id.button8);


        buttons[2][0] = (ImageButton) findViewById(R.id.button9);
        buttons[2][1] = (ImageButton) findViewById(R.id.button10);
        buttons[2][2] = (ImageButton) findViewById(R.id.button11);
        buttons[2][3] = (ImageButton) findViewById(R.id.button12);


        buttons[3][0] = (ImageButton) findViewById(R.id.button13);
        buttons[3][1] = (ImageButton) findViewById(R.id.button14);
        buttons[3][2] = (ImageButton) findViewById(R.id.button15);
        buttons[3][3] = (ImageButton) findViewById(R.id.button16);

        reset = (Button) findViewById(R.id.reset);
        photo = (Button) findViewById(R.id.photo);
        spoiler = (Button) findViewById(R.id.spoiler);
        solution =(Button) findViewById(R.id.solution);
        backNumber = (Button) findViewById(R.id.back);

        spoiler.setVisibility(View.INVISIBLE);
        backNumber.setVisibility(View.INVISIBLE);

        message = (TextView) findViewById(R.id.message);


    }

    private void setListeners() {
        reset.setOnClickListener(this);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ImageButton button = buttons[i][j];
                button.setOnClickListener(this);
            }
        }
    }

    private NonePoint getClickedPoint(ImageButton clickedButton) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (clickedButton == buttons[i][j]) {
                    NonePoint none = new NonePoint();
                    none.x = i;
                    none.y = j;
                    return none;
                }
            }
        }
        return null;
    }



    private void paintGame() {
        ArrayList<Integer> randNumb = game.generateRandomNumbers();
        int k = 0;
        int b = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ImageButton button = buttons[i][j];
                if (game.array[i][j] > -1 && button.getVisibility() == View.VISIBLE) {
                    String s = "p" +  randNumb.get(k);
                    button.setBackground(getResources().getDrawable(getResources().getIdentifier(s, "drawable", getPackageName())));
                    check[i][j] = randNumb.get(k);
                    k++;
                } else {
                    button.setVisibility(View.INVISIBLE);
                    button.setBackground(null);
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private void splitBitmap(Bitmap picture) {
        scaledBitmap = Bitmap.createScaledBitmap(picture, 752, 752, false);


        imgs[1] = Bitmap.createBitmap(scaledBitmap, 0, 0, 188 , 188);
        imgs[2] = Bitmap.createBitmap(scaledBitmap, 188, 0, 188 , 188);
        imgs[3] = Bitmap.createBitmap(scaledBitmap, 376, 0, 188 , 188);
        imgs[4] = Bitmap.createBitmap(scaledBitmap, 564, 0, 188 , 188);
        imgs[5] = Bitmap.createBitmap(scaledBitmap, 0, 188, 188 , 188);
        imgs[6] = Bitmap.createBitmap(scaledBitmap, 188, 188, 188 , 188);
        imgs[7] = Bitmap.createBitmap(scaledBitmap, 376, 188, 188 , 188);
        imgs[8] = Bitmap.createBitmap(scaledBitmap, 564, 188, 188 , 188);
        imgs[9] = Bitmap.createBitmap(scaledBitmap, 0, 376, 188 , 188);
        imgs[10] = Bitmap.createBitmap(scaledBitmap, 188, 376, 188 , 188);
        imgs[11] = Bitmap.createBitmap(scaledBitmap, 376, 376, 188 , 188);
        imgs[12] = Bitmap.createBitmap(scaledBitmap, 564, 376, 188 , 188);
        imgs[13] = Bitmap.createBitmap(scaledBitmap, 0, 564, 188 , 188);
        imgs[14] = Bitmap.createBitmap(scaledBitmap, 188, 564, 188 , 188);
        imgs[15] = Bitmap.createBitmap(scaledBitmap, 376, 564, 188 , 188);

        ArrayList<Integer> randNumb = game.generateRandomNumbers();
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ImageButton button = buttons[i][j];
                if (button.getVisibility() == View.VISIBLE) {
                    button.setBackground( new BitmapDrawable(getResources(),imgs[randNumb.get(k)]));
                    check[i][j] = randNumb.get(k);
                    k++;
                } else {
                    button.setVisibility(View.INVISIBLE);
                    button.setBackground(null);
                }
            }
        }
        flag = false;
        spoiler.setVisibility(View.VISIBLE);
        backNumber.setVisibility(View.VISIBLE);

    }

    private void resetGame(boolean f) {
        ArrayList<Integer> randNumb = game.generateRandomNumbers();
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ImageButton button = buttons[i][j];
                if (button.getVisibility() == View.VISIBLE) {
                //    button.setText("" + randNumb.get(k));
                    if(f == true) {
                        String s = "p" + randNumb.get(k);
                        button.setBackground(getResources().getDrawable(getResources().getIdentifier(s, "drawable", getPackageName())));
                    }else {
                        button.setBackground(new BitmapDrawable(getResources(), imgs[randNumb.get(k)]));
                    }
                    check[i][j] = randNumb.get(k);
                    k++;

                }
            }
        }
    }

    private void solution(boolean f){
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ImageButton button = buttons[i][j];
                if (game.array[i][j] > -1) {
                    button.setVisibility(View.VISIBLE);
                    k++;
                    if (k < 16) {
                        if (f == true) {
                            String s = "p" + k;
                            button.setBackground(getResources().getDrawable(getResources().getIdentifier(s, "drawable", getPackageName())));
                        } else {
                            button.setBackground(new BitmapDrawable(getResources(), imgs[k]));
                        }
                        check[i][j] = k;
                    }
                }
                 else {
                    button.setVisibility(View.INVISIBLE);
                    button.setBackground(null);
                    game.none.x = 3;
                    game.none.y = 3;
                }
            }
        }
    }


}