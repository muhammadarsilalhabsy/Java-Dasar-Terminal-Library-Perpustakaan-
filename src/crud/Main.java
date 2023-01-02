package crud;

import crud.controller.BookController;
import crud.repository.BookRepository;
import java.util.Scanner;
import static crud.util.Util.*;


public class Main {
  public static void main(String[] args) {

    Scanner userInput = new Scanner(System.in);
    BookRepository repo = new BookController();

    boolean isNext = true;

    while (isNext) {
      clearScreen();
      System.out.println("Database Perpustakaan\n");
      System.out.println("1.\tLihat Data Buku");
      System.out.println("2.\tCari Data Buku");
      System.out.println("3.\tTambah Data Buku");
      System.out.println("4.\tUbah Data Buku");
      System.out.println("5.\tHapus Buku");

      System.out.print("\nMasukan Input: ");
      String input = userInput.next();

      switch (input) {
        case "1" -> {
          System.out.println("===============");
          System.out.println("Lihat Data Buku");
          System.out.println("===============");
          clearScreen();
          repo.getAll();
        }
        case "2" -> {
          System.out.println("===============");
          System.out.println("Cari Data Buku");
          System.out.println("===============");
          clearScreen();
          repo.find();
        }
        case "3" -> {
          System.out.println("===============");
          System.out.println("Tambah Data Buku");
          System.out.println("===============");
          clearScreen();
          repo.add();
        }
        case "4" -> {
          System.out.println("===============");
          System.out.println("Ubah Data Buku");
          System.out.println("===============");
          clearScreen();
          repo.update();
        }
        case "5" -> {
          System.out.println("===============");
          System.out.println("Hapus Data Buku");
          System.out.println("===============");
          clearScreen();
          repo.delete();
        }
        default -> System.out.println("\nInput anda tidak diketahui!\nPilih [1 - 5]");
      }
      isNext = getYesOrNo("Apakah anda ingin melanjutkan");
    }
  }
}
