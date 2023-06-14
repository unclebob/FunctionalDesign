package gossipingBusDrivers;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.
  IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class GossipTest {
  private Stop stop1;
  private Stop stop2;
  private Stop stop3;
  private Route route1;
  private Route route2;
  private Rumor rumor1;
  private Rumor rumor2;
  private Rumor rumor3;
  private Driver driver1;
  private Driver driver2;

  @Before
  public void setUp() {
    stop1 = new Stop("stop1");
    stop2 = new Stop("stop2");
    stop3 = new Stop("stop3");
    route1 = new Route(stop1, stop2);
    route2 = new Route(stop1, stop2, stop3);
    rumor1 = new Rumor("Rumor1");
    rumor2 = new Rumor("Rumor2");
    rumor3 = new Rumor("Rumor3");
    driver1 = new Driver("Driver1", route1, rumor1);
    driver2 = new Driver("Driver2", route2, rumor2, rumor3);
  }

  @Test
  public void driverStartsAtFirstStopInRoute() throws Exception {
    assertEquals(stop1, driver1.getStop());
  }

  @Test
  public void driverDrivesToNextStop() throws Exception {
    driver1.drive();
    assertEquals(stop2, driver1.getStop());
  }

  @Test
  public void driverReturnsToStartAfterLastStop() throws Exception {
    driver1.drive();
    driver1.drive();
    assertEquals(stop1, driver1.getStop());
  }

  @Test
  public void firstStopHasDriversAtStart() throws Exception {
    assertThat(stop1.getDrivers(), containsInAnyOrder(driver1, driver2));
    assertThat(stop2.getDrivers(), empty());
  }

  @Test
  public void multipleDriversEnterAndLeaveStops() throws Exception {
    assertThat(stop1.getDrivers(), containsInAnyOrder(driver1, driver2));
    assertThat(stop2.getDrivers(), empty());
    assertThat(stop3.getDrivers(), empty());
    driver1.drive();
    driver2.drive();
    assertThat(stop1.getDrivers(), empty());
    assertThat(stop2.getDrivers(), containsInAnyOrder(driver1, driver2));
    assertThat(stop3.getDrivers(), empty());
    driver1.drive();
    driver2.drive();
    assertThat(stop1.getDrivers(), containsInAnyOrder(driver1));
    assertThat(stop2.getDrivers(), empty());
    assertThat(stop3.getDrivers(), containsInAnyOrder(driver2));
    driver1.drive();
    driver2.drive();
    assertThat(stop1.getDrivers(), containsInAnyOrder(driver2));
    assertThat(stop2.getDrivers(), containsInAnyOrder(driver1));
    assertThat(stop3.getDrivers(), empty());
  }

  @Test
  public void driversHaveRumorsAtStart() throws Exception {
    assertThat(driver1.getRumors(), containsInAnyOrder(rumor1));
    assertThat(driver2.getRumors(), containsInAnyOrder(rumor2, rumor3));  
  }

  @Test
  public void noDriversGossipAtEmptyStop() throws Exception {
    stop2.gossip();
    assertThat(driver1.getRumors(), containsInAnyOrder(rumor1));
    assertThat(driver2.getRumors(), containsInAnyOrder(rumor2, rumor3));
  }

  @Test
  public void driversGossipAtStop() throws Exception {
    stop1.gossip();
    assertThat(driver1.getRumors(), containsInAnyOrder(rumor1, rumor2, rumor3));
    assertThat(driver2.getRumors(), containsInAnyOrder(rumor1, rumor2, rumor3));
  }

  @Test
  public void gossipIsNotDuplicated() throws Exception {
    stop1.gossip();
    stop1.gossip();
    assertThat(driver1.getRumors(), containsInAnyOrder(rumor1, rumor2, rumor3));
    assertThat(driver2.getRumors(), containsInAnyOrder(rumor1, rumor2, rumor3));
  }

  @Test
  public void driveTillEqualTest() throws Exception {
    assertEquals(1, Simulation.driveTillEqual(driver1, driver2));
  }

  @Test
  public void acceptanceTest1() throws Exception {
    Stop s1 = new Stop("s1");
    Stop s2 = new Stop("s2");
    Stop s3 = new Stop("s3");
    Stop s4 = new Stop("s4");
    Stop s5 = new Stop("s5");
    Route r1 = new Route(s3, s1, s2, s3);
    Route r2 = new Route(s3, s2, s3, s1);
    Route r3 = new Route(s4, s2, s3, s4, s5);
    Driver d1 = new Driver("d1", r1, new Rumor("1"));
    Driver d2 = new Driver("d2", r2, new Rumor("2"));
    Driver d3 = new Driver("d3", r3, new Rumor("3"));
    assertEquals(6, Simulation.driveTillEqual(d1, d2, d3));
  }

  @Test
  public void acceptanceTest2() throws Exception {
    Stop s1 = new Stop("s1");
    Stop s2 = new Stop("s2");
    Stop s5 = new Stop("s5");
    Stop s8 = new Stop("s8");
    Route r1 = new Route(s2, s1, s2);
    Route r2 = new Route(s5, s2, s8);
    Driver d1 = new Driver("d1", r1, new Rumor("1"));
    Driver d2 = new Driver("d2", r2, new Rumor("2"));
    assertEquals(480, Simulation.driveTillEqual(d1, d2));
  }
}
