package progra2;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

class Doctor {
    private String nombre;
    private int idMedico;
    private String especialidad;
    private List<Paciente> pacientesAtendidos;

    public Doctor(String nombre, int idMedico, String especialidad) {
        this.nombre = nombre;
        this.idMedico = idMedico;
        this.especialidad = especialidad;
        this.pacientesAtendidos = new ArrayList<>();
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void agregarPaciente(Paciente paciente) {
        if (pacientesAtendidos.size() < 10) {
            pacientesAtendidos.add(paciente);
        } else {
            System.out.println("Error: Límite de pacientes atendidos por el doctor alcanzado.");
        }
    }

    // Getters  

    public List<Paciente> getPacientesAtendidos() {
        return pacientesAtendidos;
    }
}

class Paciente {
    private int idPaciente;
    private String nombre;
    private String fechaIngreso;

    public Paciente(int idPaciente, String nombre, String fechaIngreso) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters 
}

 class HospitalManagementSystem {
    private List<Doctor> doctores;
    private Gson gson;
    private Scanner scanner;

    public HospitalManagementSystem() {
        doctores = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        scanner = new Scanner(System.in);
    }

    public void agregarDoctor(Doctor doctor) {
        doctores.add(doctor);
    }

    public Doctor obtenerDoctor(int idMedico) {
        for (Doctor doctor : doctores) {
            if (doctor.getIdMedico() == idMedico) {
                return doctor;
            }
        }
        return null;
    }

    public int cantidadTotalPacientesAtendidos() {
        int total = 0;
        for (Doctor doctor : doctores) {
            total += doctor.getPacientesAtendidos().size();
        }
        return total;
    }

    public void guardarEnArchivo(String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(doctores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarDesdeArchivo(String nombreArchivo) {
        try (FileReader reader = new FileReader(nombreArchivo)) {
            Type doctorListType = new TypeToken<List<Doctor>>(){}.getType();
            doctores = gson.fromJson(reader, doctorListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarMenu() {
        System.out.println("Bienvenidos al Sistema del Hospital Pirulito");
        System.out.println("Seleccione una opción:");
        System.out.println("1. Agregar Doctor");
        System.out.println("2. Registrar Paciente Atendido");
        System.out.println("3. Calcular Cantidad Total de Pacientes Atendidos");
        System.out.println("4. Salir");
    }

    public static void main(String[] args) {
        HospitalManagementSystem sistema = new HospitalManagementSystem();
        sistema.cargarDesdeArchivo("doctores.json");

        boolean salir = false;

        while (!salir) {
            sistema.mostrarMenu();
            Scanner scanner = sistema.scanner;
            int opcion = scanner.nextInt();
            scanner.nextLine();  // salto de línea

            switch (opcion) {
                case 1:
                    // Agregar Doctor
                    System.out.print("Nombre del Doctor: ");
                    String nombreDoctor = scanner.nextLine();
                    System.out.print("ID del Doctor: ");
                    int idDoctor = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Especialidad del Doctor: ");
                    String especialidadDoctor = scanner.nextLine();
                    Doctor nuevoDoctor = new Doctor(nombreDoctor, idDoctor, especialidadDoctor);
                    sistema.agregarDoctor(nuevoDoctor);
                    System.out.println("Doctor agregado con éxito.");
                    break;
                case 2:
                    // Registrar Paciente Atendido
                    System.out.print("ID del Doctor que atendió al paciente: ");
                    int idDoctorAtendio = scanner.nextInt();
                    scanner.nextLine();
                    Doctor doctorAtendio = sistema.obtenerDoctor(idDoctorAtendio);
                    if (doctorAtendio != null) {
                        System.out.print("Nombre del Paciente: ");
                        String nombrePaciente = scanner.nextLine();
                        System.out.print("Fecha de Ingreso del Paciente: ");
                        String fechaIngreso = scanner.nextLine();
                        Paciente nuevoPaciente = new Paciente(sistema.cantidadTotalPacientesAtendidos() + 1, nombrePaciente, fechaIngreso);
                        doctorAtendio.agregarPaciente(nuevoPaciente);
                        System.out.println("Paciente registrado con éxito.");
                    } else {
                        System.out.println("No se encontró el doctor con el ID especificado.");
                    }
                    break;
                case 3:
                    // Calcular Cantidad Total de Pacientes Atendidos
                    int totalPacientesAtendidos = sistema.cantidadTotalPacientesAtendidos();
                    System.out.println("Cantidad total de pacientes atendidos: " + totalPacientesAtendidos);
                    break;
                case 4:
                    // Salir del programa
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción válida.");
                    break;
            }
        }

        sistema.guardarEnArchivo("doctores.json");
    }
}
