package prog;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {

    private static List<Menu> result = new ArrayList<>();
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
    private static EntityManager em = emf.createEntityManager();
    private static CriteriaBuilder cb = em.getCriteriaBuilder();
    private static CriteriaQuery<Menu> cq = cb.createQuery(Menu.class);
    private static Root<Menu> root = cq.from(Menu.class);

    public static void main( String[] args ) {

        try {
            em.getTransaction().begin();
            String name;
            double price;
            double weight;
            boolean discount;
            try{
                for (int i=1; i<101; i++){
                    name = "Dish " + i;
                    price = 10+ new BigDecimal(Math.random()*100).setScale(1, RoundingMode.UP).doubleValue();
                    weight = 50 + new BigDecimal(Math.random()*300).setScale(1, RoundingMode.UP).doubleValue();
                    discount = new Random().nextBoolean();
                    em.persist(new Menu(name,weight,price,discount));
                }

                em.getTransaction().commit();
            } catch (Exception ex) {
                em.getTransaction().rollback();
                return;
            }

            showMenu();





        }finally {
            em.close();
            emf.close();
        }
    }
    private static void showMenu(){
        Scanner sc = new Scanner(System.in);
        boolean ch = true;

        while (ch) {
            System.out.println("\t----Programm Menu----");
            System.out.println  ("\t1 - add dish to Menu\n" +
                    "\t2 - sort dishes by price\n" +
                    "\t3 - show dishes with discount\n" +
                    "\t4 - choose set of random dishes by weight\n" +
                    "\t5 - exit!");
            String choice = sc.nextLine();
            if(choice!=null&&choice.length()==1){
                switch (Integer.parseInt(choice)){
                    case 1:
                        addDish(sc);
                        showMenu();
                        break;
                    case 2:
                        sortDishesByPrice(sc);
                        showMenu();
                    case 3:
                        showDishesWithDiscount();
                        showMenu();
                        break;
                    case 4:
                        chooseRandomDishes(sc);
                        break;
                    case 5:
                        ch = false;
                        sc.close();
                        break;
                }
                System.out.println("Wrong input! Try again!");
            }

        }
    }

    private static void chooseRandomDishes(Scanner sc) {
        long idRandom;
        double maxWeight;
        double currentWeight = 0;
        double totalWight = 0;

        Menu menu;

        while(true) {
            try {

                System.out.print("Enter weight: ");
                maxWeight = Double.parseDouble(sc.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Wrong input! Try again!");
                continue;
            }
            break;
        }

        while (true){
            idRandom = (long)(Math.random()*100);
            Query query = em.createNamedQuery("Menu.getOne",Menu.class);
            query.setParameter("id", idRandom);
            menu = (Menu)query.getSingleResult();
            currentWeight+=menu.getWeight();
            if(currentWeight<maxWeight){
                totalWight+=menu.getWeight();
                result.add(menu);
            }
            else {
                System.out.println("\nOver: " +currentWeight );
                break;
            }
        }

        System.out.println("\nDishes set with weight less than "+maxWeight+"\n");
        System.out.println("Total wheight: " + (currentWeight-menu.getWeight())+"\n");

        for (Menu m:result) {
            System.out.println(m);
        }
    }

    private static void sortDishesByPrice(Scanner sc) {
        double low;
        double high;

        while(true) {
            try {

                System.out.print("Enter low price: ");
                low = Double.parseDouble(sc.nextLine());

                System.out.print("Enter high price: ");
                high = Double.parseDouble(sc.nextLine());


            } catch (NumberFormatException e) {
                System.out.println("Wrong input! Try again!");
                continue;
            }
            break;
        }

        cq.where(cb.between(root.<Double>get("price"), low, high));
        Query query = em.createQuery(cq);
        result = query.getResultList();

        System.out.println("\nPrice between "+low+" and "+high+"\n");
        for (Menu m:result) {
            System.out.println(m);
        }
        result.clear();
    }

    private static void showDishesWithDiscount() {

        cq.where(cb.equal(root.<Boolean>get("discount"), true));
        Query query = em.createQuery(cq);
        result = query.getResultList();


        System.out.println("\nDishes with discount\n");
        for (Menu m:result) {
            System.out.println(m);
        }
        result.clear();

    }

    private static void addDish(Scanner sc) {
        String name;
        double price;
        double weight;
        String discountS;
        boolean discount;

        while(true) {
            try {

                System.out.print("Dish name: ");
                name= sc.nextLine();

                System.out.print("Enter price: ");
                price = Double.parseDouble(sc.nextLine());

                System.out.print("Enter weight: ");
                weight = Double.parseDouble(sc.nextLine());

                System.out.print("Enter discount: (y/n) ");
                discountS = sc.nextLine();

                if(!discountS.equals("y")&&!discountS.equals("n")){
                    System.out.println("Wrong input! Try again!");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong input! Try again!");
                continue;
            }
            break;
        }
        discount = discountS.equals("y")?true:false;
        try{
            em.getTransaction().begin();
            em.persist(new Menu(name,weight,price,discount));
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
             return;
        }


    }
}

