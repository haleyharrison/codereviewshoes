import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;



public class Store{
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Store(String name) {
    this.name = name;
  }

  public static List<Store> all(){
    String sql = "SELECT id, name FROM stores";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }


  @Override
  public boolean equals (Object otherStore) {
    if (!(otherStore instanceof Store)) {
      return false;
    } else {
      Store newStore = (Store) otherStore;
      return this.getName().equals(newStore.getName()) &&
             this.getId() == newStore.getId();
    }
  }

  public void save(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Store find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stores WHERE id = :id";
      Store store = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Store.class);
        return store;
    }
  }

  public void updateName(String name) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stores SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

    public void delete() {
      try (Connection con = DB.sql2o.open()) {
        String deleteQuery = "DELETE FROM stores WHERE id =:id;";
          con.createQuery(deleteQuery)
            .addParameter("id", id)
            .executeUpdate();

        String joinDeleteQuery = "DELETE FROM stores_brands WHERE store_id =:store_id";
          con.createQuery(joinDeleteQuery)
            .addParameter("store_id", this.getId())
            .executeUpdate();
      }
    }

    public void addBrand(Brand brand){
        try(Connection con = DB.sql2o.open()){
          String sql = "INSERT INTO stores_brands (store_id, brand_id) VALUES (:store_id, :brand_id)";
          con.createQuery(sql)
            .addParameter("store_id", this.getId())
            .addParameter("brand_id", brand.getId())
            .executeUpdate();
        }
    }

    public List<Brand> getBrands(){
        try(Connection con = DB.sql2o.open()){
          String sql = "SELECT brands.* FROM" +
          " stores" +
          " JOIN stores_brands ON (stores.id = stores_brands.store_id)" +
          " JOIN brands ON (stores_brands.brand_id = brands.id)" +
          " WHERE stores.id = :id";
          List<Brand> brands = con.createQuery(sql)
            .addParameter("id", this.id)
            .executeAndFetch(Brand.class);
            return brands;
          }
    }




}
