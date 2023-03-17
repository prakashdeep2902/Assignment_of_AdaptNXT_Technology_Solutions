
import java.io.*;
import java.nio.file.attribute.AclEntryFlag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws IOException {
        String url = "https://www.officedepot.com/b/ergonomic-office-chairs/N-593065/";

        List<String[]> productList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements products=doc.getElementsByClass("od-container od-container-lg od-search-browse-products-vertical-layout")
                    .first().getElementsByClass("od-col od-col-xl-3 od-col-lg-4 od-col-md-6 od-col-sm-6 od-col-12 od-search-browse-products-vertical-grid-product");
            for (Element product:products)
            {
               String name=product.select("div.od-product-card-region div.od-product-card-region .od-product-card-region-description a.od-product-card-description").text();
               int index=name.indexOf(",");
                String Shortname="";
               if (index!=-1)
               {
                 Shortname=name.substring(0, index);
               }
               else {
                   continue;
               }
               String price=product.select(" div.od-product-card-region  div.od-product-card-region  div.od-product-card-region  span.od-graphql-price-big-price").text();
               String Description=name;
               if (!Shortname.isEmpty() && !price.isEmpty() && !Description.isEmpty())
               {
                   productList.add(new String[]{Shortname,price,Description});
               }

            }

           Collections.sort(productList, new Comparator<String[]>() {
               @Override
               public int compare(String[] o1, String[] o2) {
                   return Double.compare(Double.parseDouble(o1[1].substring(1)),Double.parseDouble(o2[1].substring(1)));
               }
           });

            String csvFile = "products.csv";
            try (PrintWriter writer = new PrintWriter(new File(csvFile))) {
                writer.println("Name,Price,Description");

                for (int i = 0; i < 10 && i < productList.size(); i++) {
                    String[] product = productList.get(i);
                    writer.println();
                    writer.println("Name of Product:"+product[0] );
                    writer.println("price:  " + product[1]);
                    writer.println( "Description: "+ product[2]);


                }
            }


        } catch (IOException e) {
            System.out.println(e);
        }
    }

}