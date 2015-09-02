import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class StoreTest{

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(0, Store.all().size());
  }

  @Test
  public void equals_returnsTrueIfInputIsTheSame() {
    Store myStore = new Store ("REI");
    Store myStore2 = new Store ("REI");
    assertTrue(myStore.equals(myStore2));
  }

  @Test
  public void save_savesCorrectlyIntoDatabase(){
    Store myStore = new Store ("Target");
    myStore.save();
    Store savedStore = Store.all().get(0);
    assertEquals(myStore, savedStore);
  }

  @Test
  public void getId_returnsId(){
    Store myStore = new Store ("Nine West");
    myStore.save();
    Store savedStore = Store.all().get(0);
    assertEquals(savedStore.getId(), myStore.getId());
  }

  @Test
  public void getName_returnsName() {
    Store myStore = new Store ("Shoe Pavillion");
    myStore.save();
    assertEquals("Shoe Pavillion", myStore.getName());
  }

  @Test
  public void find_findsStoreInDatabase_True() {
    Store myStore = new Store ("Payless");
    myStore.save();
    Store savedStore = Store.find(myStore.getId());
    assertTrue(myStore.equals(savedStore));
  }

  @Test
  public void updateName_changesName(){
    Store myStore = new Store ("Goodwill");
    myStore.save();
    myStore.updateName("Nordstrom");
    Store savedStore = Store.find(myStore.getId());
    assertEquals("Nordstrom", savedStore.getName());
  }


  @Test
  public void delete_DeletesFromDatabase(){
    Store myStore = new Store ("REI");
    myStore.save();
    myStore.delete();
    Store savedStore = Store.find(myStore.getId());
    assertEquals(false, myStore.equals(savedStore));
  }

  @Test
  public void getBrands_returnsBrands(){
    Brand myBrand = new Brand ("Toms");
    myBrand.save();
    Store myStore = new Store ("Target");
    myStore.save();
    myStore.addBrand(myBrand);
    assertEquals(myStore.getBrands().size(), 1);
  }

}
