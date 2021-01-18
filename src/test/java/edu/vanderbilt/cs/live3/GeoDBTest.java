package edu.vanderbilt.cs.live3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeoDBTest {

    @Test
    public void testSimpleInsert() {
        int bitsOfPrecision = 16;
        GeoDB db = GeoDBFactory.newDatabase(bitsOfPrecision);
        db.insert(0, 0);

        for(int i = 0; i < bitsOfPrecision; i++) {
            assertTrue(db.contains(0, 0, i));
        }
    }

    @Test
    public void testSimpleDelete() {
        int bitsOfPrecision = 16;
        GeoDB db = GeoDBFactory.newDatabase(bitsOfPrecision);
        db.insert(0, 0);
        db.delete(0,0);

        for(int i = 0; i < bitsOfPrecision; i++) {
            assertTrue(!db.contains(0, 0, i));
        }
    }

    @Test
    public void testZeroBits(){
        GeoDB db = GeoDBFactory.newDatabase(16);
        db.insert(0, 0);
        db.insert(90, 180);
        db.insert(-90, -180);
        db.insert(-90, 180);
        db.insert(90, -180);

        assertEquals(5, db.nearby(0,0, 0).size());
    }

    @Test
    public void testZeroBitsDelete(){
        GeoDB db = GeoDBFactory.newDatabase(16);
        db.insert(0, 0);
        db.insert(90, 180);
        db.insert(-90, -180);
        db.insert(-90, 180);
        db.insert(90, -180);

        db.deleteAll(0, 0, 0);

        assertEquals(0, db.nearby(0,0, 0).size());
    }

    @Test
    public void testInsertDeleteSeries(){

            GeoDB db = GeoDBFactory.newDatabase(16);
            db.insert(0, 0);
            db.insert(90, 180);
            db.insert(-90, -180);
            db.insert(-90, 180);
            db.insert(90, -180);
            assertTrue(db.contains(0, 0, 16));
            assertTrue(db.contains(90, 180, 16));
            assertTrue(db.contains(-90, -180, 16));
            assertTrue(db.contains(-90, 180, 16));
            assertTrue(db.contains(90, -180, 16));
            assertTrue(db.contains(90.5, -180.5, 16));
            assertTrue(!db.contains(1, -1, 16));
            assertTrue(!db.contains(45, -45, 16));

            db.delete(90, -180);
            assertTrue(!db.contains(90, -180, 16));

            db.deleteAll(1, 1, 1);
            assertTrue(db.contains(-90, -180, 16));
            assertTrue(!db.contains(90, 180, 16));
            db.insert(90, 180);
            assertTrue(db.contains(90, 180, 16));

    }

}
