package com.facememo;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DbGenerator extends DaoGenerator{
    private static final int DB_VERSION = 2;

    public static void main(String[] args) {
        Schema schema = new Schema(DB_VERSION, "lenguyenthanh.facememo.data.entities");
        schema.setDefaultJavaPackageDao("lenguyenthanh.facememo.data.dao");
        schema.enableKeepSectionsByDefault();

        Entity contactEntity = schema.addEntity("Contact");
        contactEntity.addLongProperty("id").primaryKey().autoincrement();
        contactEntity.addStringProperty("firstName");
        contactEntity.addStringProperty("lastName");
        contactEntity.addStringProperty("number");
        contactEntity.addStringProperty("note");
        contactEntity.addStringProperty("photo");
        contactEntity.addIntProperty("rawContactId");

        try {
            new DbGenerator().generateAll(schema, "src-gen/main/java", "src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DbGenerator() throws IOException {}

}
