package me.tinchx.levelrank.utils;

public class Checker {

    public Checker(boolean b, Factor factor) {
        if (b) {
            try {
                factor.execute();
            } catch (Exception ignored) {

            }
        }
    }

    @FunctionalInterface
    public static interface Factor {

        public abstract void execute();
    }
}