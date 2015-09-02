import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Brand {
  private int id;
  private String brand_name;

  public int getId(){
    return id;
  }

  public String getBrandName(){
    return brand_name;
  }

  public Brand(String brand_name){
    this.brand_name = brand_name;
  }

  public static List<Brand> all(){
    String sql = "SELECT id, brand_name FROM brands";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Brand.class);
    }
  }

  @Override
  public boolean equals (Object otherBrand) {
    if (!(otherBrand instanceof Brand)) {
      return false;
    } else {
      Brand newBrand = (Brand) otherBrand;
      return this.getBrandName().equals(newBrand.getBrandName()) &&
             this.getId() == newBrand.getId();
    }
  }

  public void save(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands (brand_name) VALUES (:brand_name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("brand_name", brand_name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Brand find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM brands WHERE id = :id";
      Brand brand = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Brand.class);
      return brand;
    }
  }

  public void updateBrand(String brand_name) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE brands SET brand_name = :brand_name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("brand_name", brand_name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()){
      String deleteQuery = "DELETE FROM brands WHERE id=:id";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM stores_brands WHERE brand_id =:brand_id";
        con.createQuery(joinDeleteQuery)
          .addParameter("brand_id", this.getId())
          .executeUpdate();
    }
  }

  public List<Store> getStores(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT stores.* FROM"+
       " brands" +
      " JOIN stores_brands ON (brands.id = stores_brands.brand_id)" +
      " JOIN stores ON (stores_brands.store_id = stores.id)" +
      " WHERE brands.id =:id";
      List<Store> stores = con.createQuery(sql)
          .addParameter("id", this.id)
          .executeAndFetch(Store.class);
          return stores;

    }
  }

  public void addStore(Store store){
      try(Connection con = DB.sql2o.open()){
        String sql = "INSERT INTO stores_brands (store_id, brand_id) VALUES (:store_id, :brand_id)";
        con.createQuery(sql)
          .addParameter("store_id", store.getId())
          .addParameter("brand_id", id)
          .executeUpdate();
      }
  }

  //HI, we forgot to add "(store_id, brand_id)" on line 109. So this method wasn't adding anything to brand_id.

}
