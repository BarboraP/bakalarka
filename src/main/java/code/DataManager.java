package code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import graphics_controls.DraggableGate;


import java.io.*;

public class DataManager {
    private Gson gson;
    private RuntimeTypeAdapterFactory<LogicGate> adapter;


    public DataManager() {

        adapter =
                RuntimeTypeAdapterFactory.
                        of(LogicGate.class) // Here you specify which is the parent class and what field particularizes the child class.
                        .registerSubtype(And_Gate.class) // if the flag equals the class name, you can skip the second parameter. This is only necessary, when the "type" field does not equal the class name.
                        .registerSubtype(Nand_Gate.class)
                        .registerSubtype(Nor_Gate.class)
                        .registerSubtype(Or_Gate.class)
                        .registerSubtype(Xor_Gate.class)
                        .registerSubtype(Input.class)
                        .registerSubtype(Output.class);

         gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
    }


    public void saveGate(DraggableGate gate, String filename) throws IOException {

        String filepath = "./" + filename + "_Gates";
        File f = new File(filepath);

        if (f.exists()) {
            f.delete();
            f = new File(filepath);
        }

        String gateJson = gson.toJson(gate);

        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true));

        bw.write(gateJson);
        bw.newLine();
        bw.close();
    }

    public void loadGates(String filename) throws IOException {
        String filepath = "./" + filename + "_Gates";

        BufferedReader br = new BufferedReader(new FileReader(filepath));

        String b = br.readLine();
        if (b != null) {
            DraggableGate gate = gson.fromJson(b, DraggableGate.class);
        }
    }


    public void saveCircuit(LogicCircuit c, String filename) throws IOException {
        String filepath = "./" + filename + "_Circuit.txt";
        File f = new File(filepath);

        if (f.exists()) {
            f.delete();
            f = new File(filepath);
        }

        String circuit = gson.toJson(c);

//        String circuit = "miau miau";
        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true));

        bw.write(circuit);
        bw.newLine();
        bw.close();
    }

    public void loadCircuit(String filename) throws IOException {
        String filepath = "./" + filename + "_Circuit.txt";

        BufferedReader br = new BufferedReader(new FileReader(filepath));

        LogicCircuit c = gson.fromJson(br, LogicCircuit.class);
        c.setConnections();
        //System.out.println("miau");
    }


}
