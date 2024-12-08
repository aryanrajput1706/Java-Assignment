package problem3;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    //Revolution period in LTU
    private static final int PERIOD_OF_ARRAKIS = 12;
    private static final int PERIOD_OF_GIEDI = 60;
    // Angular Difference (in degrees)
    private static final int ALIGNMENT_THRESHOLD = 10;

    //Current positions of planet
    private static int arrakisPosition = 0;
    private static int giediPosition = 0;
    // Atomic time unit
    private static int timeUnit = 0;

    private static final String SAMPLE_INSTRUCTIONS = "><>><</<<>>/"; // Sample instructions
    private static final String TRANSMISSION_FILE = "trans.mxt";
    private static final String ACKNOWLEDGEMENT_FILE = "recvrs.mxt";

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        // Create threads for the base station and receiver
        Thread baseStation = new Thread(new BaseStation());
        Thread receiver = new Thread(new Receiver());

        // Start the threads
        baseStation.start();
        receiver.start();

        try {
            // Wait for threads to finish
            baseStation.join();
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //calculating planetary positions
    private static boolean isAligned() {
        lock.lock();
        try {
            // Calculating positions in degrees
            arrakisPosition = (timeUnit * 360 / PERIOD_OF_ARRAKIS) % 360;
            giediPosition = (timeUnit * 360 / PERIOD_OF_GIEDI) % 360;

            // Calculating the angular difference
            int angularDifference = Math.abs(arrakisPosition - giediPosition);
            angularDifference = Math.min(angularDifference, 360 - angularDifference);

            return angularDifference <= ALIGNMENT_THRESHOLD;
        } finally {
            lock.unlock();
        }
    }

    // Base station thread
    static class BaseStation implements Runnable {
        @Override
        public void run() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSMISSION_FILE))) {
                for (char instruction : SAMPLE_INSTRUCTIONS.toCharArray()) {
                    lock.lock();
                    try {
                        // Wait until planets are aligned
                        while (!isAligned()) {
                            timeUnit++;
                            Thread.sleep(100); // Simulate time passage
                        }

                        // Write instruction to file
                        writer.write(instruction);
                        writer.newLine();
                        System.out.println("Base Station: Sent instruction " + instruction);

                        timeUnit++; // Advance time after sending
                    } finally {
                        lock.unlock();
                    }

                    // Simulate processing delay
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Receiver thread
    static class Receiver implements Runnable {
        @Override
        public void run() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACKNOWLEDGEMENT_FILE));
                 BufferedReader reader = new BufferedReader(new FileReader(TRANSMISSION_FILE))) {
                String instruction;
                while (true) {
                    lock.lock();
                    try {
                        // Wait until planets are aligned and a new instruction is available
                        while (!isAligned() || (instruction = reader.readLine()) == null) {
                            if (timeUnit >= SAMPLE_INSTRUCTIONS.length()) {
                                return; // Exit when all instructions are processed
                            }
                            timeUnit++;
                            Thread.sleep(100); // Simulate time passage
                        }

                        // Process the instruction
                        System.out.println("Receiver: Received instruction " + instruction);
                        writer.write(instruction);
                        writer.newLine();
                        System.out.println("Receiver: Acknowledged instruction " + instruction);

                        timeUnit++; // Advance time after processing
                    } finally {
                        lock.unlock();
                    }

                    // Simulate processing delay
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}