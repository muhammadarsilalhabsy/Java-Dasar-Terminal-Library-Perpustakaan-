package crud.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Util {

  public static boolean getYesOrNo(String massage) {

    Scanner userInput = new Scanner(System.in);

    System.out.print(massage + " (y/n)? ");
    String input = userInput.nextLine();

    while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
      System.out.println("Pilihan anda bukan 'y' atau 'n'!");
      System.out.print(massage + " (y/n)? ");
      input = userInput.nextLine();
    }

    return input.equalsIgnoreCase("y");
  }

  public static void clearScreen() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033\143");
      }
    } catch (Exception e) {
      System.out.println("Tidak bisa clear screen!");
    }
  }

  public static boolean checkDataIfExist(String[] keywords, boolean isDisplay) throws IOException {

    BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"));

    String data = bufferedReader.readLine();
    boolean isExsist = false;
    int count = 0;

    if (isDisplay) {
      System.out.println("\n| NO |\tTahun |\tPenulis               |\tPenerbit              |\tJudul Buku                              |");
      System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    while (data != null) {

      isExsist = true;

      for (String keyword : keywords) {
        isExsist &= data.toLowerCase().contains(keyword.toLowerCase());
      }

      if (isExsist) {
        if(isDisplay){
          StringTokenizer stringToken = new StringTokenizer(data, ",");
          stringToken.nextToken();
          System.out.printf("| %2d ", ++count);
          System.out.printf("|\t%4s  ", stringToken.nextToken());
          System.out.printf("|\t%-20s  ", stringToken.nextToken());
          System.out.printf("|\t%-20s  ", stringToken.nextToken());
          System.out.printf("|\t%-35s  \t|\n", stringToken.nextToken());
        }else{
          break;
        }
      }
      data = bufferedReader.readLine();
    }
    if (isDisplay) {
      System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    return isExsist;
  }

  public static int getEntryPerYear(String penulis, String tahun) throws IOException {
    FileReader fileReader = new FileReader("database.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    int result = 0;
    Scanner dataScanner;
    String primaryKey;
    String data = bufferedReader.readLine();

    while (data != null) {

      // potong data
      dataScanner = new Scanner(data);
      dataScanner.useDelimiter(",");

      primaryKey = dataScanner.next();

      // potong primary key
      dataScanner = new Scanner(primaryKey);
      dataScanner.useDelimiter("_");

      penulis = penulis.replaceAll("\\s+", "");

      if (penulis.equalsIgnoreCase(dataScanner.next()) && tahun.equalsIgnoreCase(dataScanner.next())) {
        result = dataScanner.nextInt(); // mengembalikan integer
      }
      data = bufferedReader.readLine();
    }

    return result;
  }

  public static String yearValidation() {
    Scanner input = new Scanner(System.in);
    String tahun = input.nextLine();

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy");
    boolean tahunValid = false;
    while (!tahunValid) {
      try {
        fmt.parse(tahun);
        tahunValid = true;
      } catch (Exception e) {
        System.err.println("\nPenulisan tahun yang anda masukan salah!, Format (YYYY)");
        System.out.print("Silahkan masukan tahun lagi: ");
        tahun = input.next();
      }
    }
    return tahun;
  }
}
