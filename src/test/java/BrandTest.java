import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class BrandTest{

 @Rule
 public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(0, Brand.all().size());
  }

  @Test
  public void equals_returnsTrueIfInputIsTheSame() {
    Brand myBrand = new Brand ("Nike");
    Brand myBrand2 = new Brand ("Nike");
    assertTrue(myBrand.equals(myBrand2));
  }

  @Test
  public void save_savesCorrectlyIntoDatabase(){
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    Brand savedBrand = Brand.all().get(0);
    assertEquals(myBrand, savedBrand);
  }

  @Test
  public void getId_returnsId(){
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    Brand savedBrand = Brand.all().get(0);
    assertEquals(savedBrand.getId(), myBrand.getId());
  }

  @Test
  public void getBrandName_returnsBrandName(){
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    Brand savedBrand = Brand.all().get(0);
    assertEquals(savedBrand.getBrandName(), myBrand.getBrandName());
  }

  @Test
  public void find_findsBrandInDatabase_True() {
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertTrue(myBrand.equals(savedBrand));
  }

  @Test
  public void updateBrand_changesBrand() {
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    myBrand.updateBrand("Adidas");
    Brand savedBrand = Brand.find(myBrand.getId());
    assertEquals("Adidas", savedBrand.getBrandName());
  }

  @Test
  public void delete_DeletesFromDatabase(){
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    myBrand.delete();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertEquals(false, myBrand.equals(savedBrand));
  }

  @Test
  public void getStores_returnsStores(){
    Store myStore = new Store ("REI");
    myStore.save();
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    myBrand.addStore(myStore);
    assertEquals(myBrand.getStores().size(), 1);
  }


}
