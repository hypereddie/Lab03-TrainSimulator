package com.company.gui;


import com.company.Station;
import com.company.TrainRouteBuilder;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class SwingGUI extends Canvas {
    private TrainRouteBuilder builder;
    private BufferStrategy bf;

    public SwingGUI(TrainRouteBuilder builder) {
        this.builder = builder;
    }

    public void start() {
        createBufferStrategy(2);
        bf = getBufferStrategy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    render();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void render() {
        Graphics g = bf.getDrawGraphics();
        BufferedImage temp = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics tg = temp.getGraphics();

        tg.setColor(Color.WHITE);
        tg.fillRect(0, 0, 1024, 720);

        renderStations(tg);
        renderPassengers(tg);

        g.drawImage(temp, 0, 0, null);

        g.dispose();
        tg.dispose();
        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }

    private void renderStations(Graphics g) {
        int minX = 100;
        int maxX = 800;

        Station[] inbound = builder.buildInbound();
        Station[] outbound = builder.buildOutbound();

        g.setColor(Color.BLUE);
        int toAdd = (maxX - minX) / (inbound.length - 1);
        drawStation(minX, 200, g);
        for (int i = 1; i < inbound.length; i++) {
            drawStation(minX + (i * toAdd), 200, g);
        }

        g.setColor(Color.GREEN);
        g.drawLine(minX + 16, 200 + 16, maxX + 16, 200 + 16);

        g.setColor(Color.BLUE);
        toAdd = (maxX - minX) / (outbound.length - 1);
        drawStation(minX, 400, g);
        for (int i = 1; i < inbound.length; i++) {
            drawStation(minX + (i * toAdd), 400, g);
        }

        g.setColor(Color.GREEN);
        g.drawLine(minX + 16, 400 + 16, maxX + 16, 400 + 16);
    }

    private void renderPassengers(Graphics g) {
        int minX = 100;
        int maxX = 800;

        Station[] inbound = builder.buildInbound();
        Station[] outbound = builder.buildOutbound();

        g.setColor(Color.RED);
        int toAdd = (maxX - minX) / (inbound.length - 1);
        for (int i = 0; i < inbound.length; i++) {
            int x = minX + (i * toAdd);
            int y = 200;
            Station s = inbound[i];
            for (int z = 0; z < s.getTrainLine().getSizeOfQueue(); z++) {
                y -= 8;
                drawPassenger(x, y, g);

                if (z % 22 == 0 && z != 0) {
                    y = 200;
                    x += 8;
                }
            }
        }

        toAdd = (maxX - minX) / (outbound.length - 1);
        for (int i = 0; i < outbound.length; i++) {
            int x = minX + (i * toAdd);
            int y = 420;
            Station s = outbound[i];
            for (int z = 0; z < s.getTrainLine().getSizeOfQueue(); z++) {
                y += 8;
                drawPassenger(x, y, g);

                if (z % 22 == 0 && z != 0) {
                    y = 420;
                    x += 8;
                }
            }
        }
    }

    private void drawPassenger(int x, int y, Graphics g) {
        g.fillRect(x + (4 / 2), y + (4 / 2), 4, 4);
    }

    private void drawStation(int x, int y, Graphics g) {
        g.fillOval(x + (16 / 2), y + (16 / 2), 16, 16);
    }
}
