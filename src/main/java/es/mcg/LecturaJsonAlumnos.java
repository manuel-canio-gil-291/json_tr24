package es.mcg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class LecturaJsonAlumnos {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ENCODE = "UTF-8";

    public static class Json 
    {
        /** Attribute - mapper */
        private static ObjectMapper MAPPER;

        /**
         * @return a new instance of ObjectMapper
         */
        public static ObjectMapper mapper()
        {
            if(Json.MAPPER == null)
            {
                Json.MAPPER = Json.createJson();
            }
            return Json.MAPPER;
        }

        /**
         * @return the Object Mapper
         */
        public static ObjectMapper createJson() 
        {
            return new ObjectMapper();
        }
    }

    public static class Alumno
    {
        private String nombre;
        private int edad;
        private double calificacion;
        private boolean unidadesPendientes;
        public Alumno(String nombre, int edad, double calificacion, boolean unidadesPendientes) 
        {
            this.nombre = nombre;
            this.edad = edad;
            this.calificacion = calificacion;
            this.unidadesPendientes = unidadesPendientes;
        }
        public String getNombre() 
        {
            return nombre;
        }
        public void setNombre(String nombre) 
        {
            this.nombre = nombre;
        }
        public int getEdad() 
        {
            return edad;
        }
        public void setEdad(int edad) 
        {
            this.edad = edad;
        }
        public double getCalificacion() 
        {
            return calificacion;
        }
        public void setCalificacion(double calificacion) 
        {
            this.calificacion = calificacion;
        }
        public boolean isUnidadesPendientes() 
        {
            return unidadesPendientes;
        }
        public void setUnidadesPendientes(boolean unidadesPendientes) 
        {
            this.unidadesPendientes = unidadesPendientes;
        }
        @Override
        public String toString() 
        {
            return "Nombre: " + nombre + "\nEdad: " + edad + "\nCalificacion: " + calificacion
                    + "\nUnidades pendientes: " + unidadesPendientes + "\n";
        }
    }
    public static void main(String[] args) throws AlumnoException 
    {
        File file = null;
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        String fileContent;
        String nombre = "", mejor = "";
        int edad = 0;
        double calificacion = 0.0, calificacionM = 0.0;
        boolean unidadesPendientes = false;
        Alumno alumno = null;
        List<Alumno> alumnosPendientes = null;
        try 
        {
            file = new File("alumnos.json");
            fileContent = FileUtils.readFileToString(file, LecturaJsonAlumnos.ENCODE);
            fileWriter = new FileWriter(new File("salida-alumnos.txt"));
            printWriter = new PrintWriter(fileWriter);
            alumnosPendientes = new ArrayList<Alumno>();
            JsonNode rootJsonNode = Json.mapper().readTree(fileContent);
            if(rootJsonNode.isArray())
            {
                JsonNode rootArrayJsonNode = (ArrayNode) rootJsonNode;

                final Iterator<JsonNode> iterator = rootArrayJsonNode.elements();
                while(iterator.hasNext())
                {
                    final JsonNode alumnosJsonNode = iterator.next();
                    if(alumnosJsonNode.has("nombre"))
                    {
                        final JsonNode markNode = alumnosJsonNode.get("nombre");
                        nombre = markNode.asText();
                    }
                    if(alumnosJsonNode.has("edad"))
                    {
                        final JsonNode markNode = alumnosJsonNode.get("edad");
                        edad = Integer.parseInt(markNode.asText());
                    }
                    if(alumnosJsonNode.has("calificacion"))
                    {
                        final JsonNode markNode = alumnosJsonNode.get("calificacion");
                        calificacion = Double.parseDouble(markNode.asText());
                        if(calificacionM == 0)
                        {
                            calificacionM = calificacion;
                            mejor = nombre;
                        }
                        else if(calificacion > calificacionM)
                        {
                            calificacionM = calificacion;
                            mejor = nombre;
                        }
                    }
                    if(alumnosJsonNode.has("unidadesPendientes"))
                    {
                        final JsonNode markNode = alumnosJsonNode.get("unidadesPendientes");
                        unidadesPendientes = Boolean.parseBoolean(markNode.asText());
                    }
                    alumno = new Alumno(nombre, edad, calificacion, unidadesPendientes);
                    
                    if(unidadesPendientes)
                    {
                        alumnosPendientes.add(alumno);
                    }
                }
                Collections.sort(alumnosPendientes, Collections.reverseOrder());
                
            }
            printWriter.println("Lista de alumnos pendientes: ");
            printWriter.println(alumnosPendientes);
            printWriter.print("Alumno mas cercano a la nota media de clase: ");
            printWriter.println(mejor);

        } 
        catch (IOException ioException) 
        {
            LOGGER.error("Error mientras se trataba de leer el fichero "+file, ioException);
            throw new AlumnoException("Error mientras se trataba de leer el fichero "+file, ioException);
        }
        finally
        {
            if(printWriter != null)
            {
                printWriter.close();
            }
            if(fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch(IOException ioException2)
                {
                    LOGGER.error("No se pudo cerrar el FileWriter", ioException2);
                    throw new AlumnoException("No se pudo cerrar el FileWriter", ioException2);
                }
            }
        }
    }
}
