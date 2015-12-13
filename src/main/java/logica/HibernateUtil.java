/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author susana
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            Configuration configuration = new AnnotationConfiguration().configure("hibernate2.cfg.xml");
            configuration.addSqlFunction("group_concat", new StandardSQLFunction("group_concat", new StringType()));

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


}
