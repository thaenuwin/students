package test.students.utils.search;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

@Log4j2
public class PathFactory {


    private PathFactory(){}

    public static Path retrievePath(String prop, Object q){
        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{
                f.setAccessible(true);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof Path){
                        return (Path)obj;
                    }else{
                        throw new IllegalArgumentException("Invalid value type: "+prop);
                    }
                }
            }catch(Exception ex){
                log.error( ex );
            }
        }


        return null;
    }

    public static StringPath retireveStringPath(String prop, Object q){

        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{
                f.setAccessible(true);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof StringPath){
                        return (StringPath)obj;
                    }else{
                        throw new IllegalArgumentException("Invalid value type: "+prop);
                    }
                }
            }catch(Exception ex){
                log.error( ex );
            }
        }


        return null;
    }
    public static NumberPath retireveIntegerPath(String prop, Object q){


        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{
                f.setAccessible(true);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof NumberPath){
                        return (NumberPath)obj;
                    }else{
                        throw new IllegalArgumentException("Invalid value type: "+prop);
                    }
                }
            }catch(Exception ex){
                log.error( ex );
            }
        }


        return null;
    }

    public static DatePath retireveDatePath(String prop, Object q) {


        Field[] fields = q.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                String name = f.getName();
                if (prop.equals(name)) {
                    Object obj = f.get(q);
                    if (obj instanceof DatePath) {
                        return (DatePath) obj;
                    }
                }
            } catch (Exception ex) {
                log.error( ex );
            }
        }


        return null;
    }


    public static DateTimePath retirieveDateTimePath(String prop, Object q){


        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{
                f.setAccessible(true);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof DateTimePath){
                        return (DateTimePath)obj;
                    }else{
                        throw new IllegalArgumentException("Invalid value type: "+prop);
                    }
                }
            }catch(Exception ex){
                log.error( ex );
            }
        }


        return null;
    }


    public static TimePath retrieveTimePath(String prop, Object q){
        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{
                f.setAccessible(true);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof TimePath){
                        return (TimePath)obj;
                    }else{
                        throw new IllegalArgumentException("Invalid value type: "+prop);
                    }
                }
            }catch(Exception ex){
                log.error( ex );
            }
        }


        return null;
    }

    public static ListPath retrieveListPath(String prop, Object queryEntity){
        Field[] fields=queryEntity.getClass().getDeclaredFields();
        for(Field f:fields){
            try{
                f.setAccessible(true);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(queryEntity);
                    if(obj instanceof ListPath){
                        return (ListPath)obj;
                    }else{
                        throw new IllegalArgumentException("Invalid value type: "+prop);
                    }
                }
            }catch(Exception ex){
                log.error( ex );
            }
        }
        return null;
    }
}
