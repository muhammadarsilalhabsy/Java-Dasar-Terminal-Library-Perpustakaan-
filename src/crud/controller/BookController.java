package crud.controller;

import crud.repository.BookRepository;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

import static crud.util.Util.*;

public class BookController implements BookRepository {

  @Override
  public void getAll() {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {

      String data = bufferedReader.readLine();

      int count = 0;
      System.out.println("\n| NO |\tTahun |\tPenulis               |\tPenerbit              |\tJudul Buku                              |");
      System.out.println("---------------------------------------------------------------------------------------------------------");

      while (data != null) {
        StringTokenizer stringToken = new StringTokenizer(data, ",");
        stringToken.nextToken();
        System.out.printf("| %2d ", ++count);
        System.out.printf("|\t%4s  ", stringToken.nextToken());
        System.out.printf("|\t%-20s  ", stringToken.nextToken());
        System.out.printf("|\t%-20s  ", stringToken.nextToken());
        System.out.printf("|\t%-35s  \t|\n", stringToken.nextToken());
        data = bufferedReader.readLine();
      }
      System.out.println("---------------------------------------------------------------------------------------------------------");

    } catch (IOException e) {
      System.out.println("Database tidak ditemukan!");
      System.out.println("Silahkan tambahkan buku terlebih dahulu");
      add();
    }
  }

  @Override
  public void find() {
    try {
      Scanner userInput = new Scanner(System.in);
      File file = new File("database.txt");

      System.out.print("Masukan katakunci untuk mencari buku:");

      String input = userInput.nextLine();

      // mengambil keywords
      String[] keywords = input.split("\\s+");

      checkDataIfExist(keywords, true);

    } catch (Exception e) {
      System.err.println("Database tidak ditemukan");
      System.err.println("Silahkan isi data terlebih dahulu!");
    }
  }

  @Override
  public void add() {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt", true))) {
      Scanner input = new Scanner(System.in);

      String penulis, penerbit, judul, tahun;

      System.out.print("Masukan nama penulis: ");
      penulis = input.nextLine();

      System.out.print("Masukan nama penerbit: ");
      penerbit = input.nextLine();

      System.out.print("Masukan judul buku: ");
      judul = input.nextLine();

      System.out.print("Masukan tahun terbit (YYYY): ");
      tahun = yearValidation();

      String[] keywords = {tahun + "," + penulis + "," + penerbit + "," + judul};

      boolean isExsist = checkDataIfExist(keywords, false);

      // menulis buku di database
      if (!isExsist) {

        int entry = getEntryPerYear(penulis, tahun) + 1;

        String rebuildPenulis = penulis.replaceAll("\\s+", "");
        String primarykey = rebuildPenulis + "_" + tahun + "_" + entry;

        System.out.println("\nData yang akan anda masukan adalah");
        System.out.println("==================================");

        System.out.println("Primary key  : " + primarykey);
        System.out.println("Penulis      : " + penulis);
        System.out.println("Penerbit     : " + penerbit);
        System.out.println("Judul        : " + judul);
        System.out.println("Tahun terbit : " + tahun);

        boolean isTambahkan = getYesOrNo("Apakah anda ingin menambahkan data ini");
        if (isTambahkan) {
          bufferedWriter.write(primarykey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
          bufferedWriter.newLine();
          bufferedWriter.flush();
        }
      } else {
        System.out.println("\nData yang anda coba tambahkan sudah ada di database kita sebelumnya!\nberikut datanya:");
        checkDataIfExist(keywords, true);
      }
    } catch (IOException e) {
      System.out.println("Error with massage : " + e.getMessage());
    }
  }

  @Override
  public void update() {

    // membaca original file
    File database = new File("database.txt");

    // membuat temporary file
    File tempDB = new File("tempDB.txt");

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(database));
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempDB))) {

      // tampilkan data
      System.out.println("List data buku");
      getAll();

      Scanner input = new Scanner(System.in);
      System.out.print("\nMasukan nomor buku yang akan di ubah: ");
      int userInput = input.nextInt();

      String data = bufferedReader.readLine();
      int entryCounts = 0;

      boolean isFound = false;

      // looping
      while (data != null) {
        entryCounts++;

        if (entryCounts == userInput) {
          StringTokenizer tokenizer = new StringTokenizer(data, ",");

          System.out.println("\nData yang akan anda ingin ubah adalah");
          System.out.println("==================================");
          System.out.println("Primary key  : " + tokenizer.nextToken());
          System.out.println("Tahun terbit : " + tokenizer.nextToken());
          System.out.println("Penulis      : " + tokenizer.nextToken());
          System.out.println("Penerbit     : " + tokenizer.nextToken());
          System.out.println("Judul        : " + tokenizer.nextToken());

          isFound = true;
          String[] component = {"tahun", "penulis", "penerbit", "judul"};
          String[] tempData = new String[component.length];

          // refresh token
          tokenizer = new StringTokenizer(data, ",");
          String originalData = tokenizer.nextToken();


          for (int i = 0; i < component.length; i++) {
            originalData = tokenizer.nextToken();

            boolean isUpdate = getYesOrNo("Apakah anda ingin mengubah " + component[i]);
            if (isUpdate) {
              input = new Scanner(System.in);
              System.out.print("Masukan " + component[i] + " baru: ");
              tempData[i] = input.nextLine();
            } else {
              tempData[i] = originalData;
            }
          }

          // tampilkan data yang akan ditambahkan.
          tokenizer = new StringTokenizer(data, ",");
          tokenizer.nextToken();

          String tahun = tempData[0];
          String penulis = tempData[1];
          String penerbit = tempData[2];
          String judul = tempData[3];

          System.out.println("\nBerikut data hasil updatean anda:");
          System.out.println("==================================");
          System.out.println("Tahun terbit : " + tokenizer.nextToken() + " => " + tahun);
          System.out.println("Penulis      : " + tokenizer.nextToken() + " => " + penulis);
          System.out.println("Penerbit     : " + tokenizer.nextToken() + " => " + penerbit);
          System.out.println("Judul        : " + tokenizer.nextToken() + " => " + judul);

          boolean isSimpan = getYesOrNo("Apakah anda ingin menyimpan data ini ke database");

          if (isSimpan) {

            boolean isExsist = checkDataIfExist(tempData, false);

            if (isExsist) {
              System.err.println("Data buku yang anda update sudah ada didatabase!\nSilahkan didelete terlebih dahulu");
              // mencopy
              bufferedWriter.write(data);
              bufferedWriter.newLine();
            } else {
              int entry = getEntryPerYear(penulis, tahun) + 1;
              String rebuildPenulis = penulis.replaceAll("\\s+", "");
              String primarykey = rebuildPenulis + "_" + tahun + "_" + entry;
              bufferedWriter.write(primarykey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
            }
          } else {
            // mencopy
            bufferedWriter.write(data);
          }
        } else {
          // mencopy
          bufferedWriter.write(data);
        }
        data = bufferedReader.readLine();
        bufferedWriter.newLine();
      }

      if (!isFound) {
        System.err.println("\nBuku yang ada ingin update tidak ditemukan\nPastikan nomor buku ada atau tersedia !\n");
      }

      bufferedWriter.flush();

      // menghapus file database.txt
      database.delete();

      // merubah nama tempDB.txt ke databese.txt
      tempDB.renameTo(database);
    } catch (IOException e) {
      System.out.println("Error with massage: " + e.getMessage());
    }

  }

  @Override
  public void delete() {

    // baca file ori
    File database = new File("database.txt");

    // baca file temp
    File temp = new File("tempDB.txt");

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(database)); BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp))) {

      // tampilkan data
      System.out.println("\nList data buku di database");
      getAll();

      // ambil input
      Scanner input = new Scanner(System.in);
      System.out.print("\nMasukan nomor data yang akan anda delete: ");
      int index = input.nextInt();

      boolean isFound = false;

      int entryCounts = 0;
      String data = bufferedReader.readLine();

      // looping data
      while (data != null) {
        entryCounts++;

        boolean isDelete = false;

        if (entryCounts == index) {

          StringTokenizer tokenizer = new StringTokenizer(data, ",");

          System.out.println("\nData yang akan anda ingin hapus adalah");
          System.out.println("==================================");
          System.out.println("Primary key  : " + tokenizer.nextToken());
          System.out.println("Penulis      : " + tokenizer.nextToken());
          System.out.println("Penerbit     : " + tokenizer.nextToken());
          System.out.println("Judul        : " + tokenizer.nextToken());
          System.out.println("Tahun terbit : " + tokenizer.nextToken());

          isDelete = getYesOrNo("Apakah anda ingin mengapus data ini");
          isFound = true;
        }

        if (isDelete) {
          System.out.println("Data berhasil di hapus!");
        } else {
          bufferedWriter.write(data);
          bufferedWriter.newLine();
        }

        data = bufferedReader.readLine();
      }

      if (!isFound) {
        System.err.println("Buku tidak di temukan!\nPastikan nomor buku ada atau tersedia !\n");
      }

      // menulis ke tempDB
      bufferedWriter.flush();

      // menghapus file database.txt
      database.delete();

      // merubah nama tempDB.txt ke databese.txt
      temp.renameTo(database);

    } catch (IOException e) {
      System.out.println("Error with massage: " + e.getMessage());
    }
  }
}
