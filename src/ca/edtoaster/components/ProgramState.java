package ca.edtoaster.components;

import ca.edtoaster.utils.SegmentationFault;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import static ca.edtoaster.utils.ProgramUtils.INFINITELY_LARGE_SIZE;

public class ProgramState {

    private int ptr;
    private int programPtr;
    private byte[] buf;
    private Program program;

    private Stack<Integer> loopStack;

    private ProgramState() {}

    public static ProgramState defaultState(Program program) {
        ProgramState state = new ProgramState();
        state.ptr = 0;
        state.programPtr = 0;
        state.buf = new byte[INFINITELY_LARGE_SIZE];
        state.loopStack = new Stack<>();
        state.program = program;
        return state;
    }

    private ProgramState offsetPointer(int offset) {
        int newPtr = this.ptr + offset;
        if (newPtr < 0 || newPtr >= buf.length)
            throw new SegmentationFault(String.format("PC: %s", this.programPtr));

        this.ptr = newPtr;
        return this;
    }

    public ProgramState decrementPointer() {
        return offsetPointer(-1);
    }

    public ProgramState incrementPointer() {
        return offsetPointer(1);
    }

    private ProgramState offsetData(byte offset) {
        buf[ptr] += offset;
        return this;
    }

    public ProgramState incrementData() {
        return offsetData((byte) 1);
    }

    public ProgramState decrementData() {
        return offsetData((byte) -1);
    }

    public ProgramState getData(InputStream inputStream) {
        byte byteData = 0;
        try {
            byteData = (byte) inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buf[ptr] = byteData;
        return this;
    }

    public ProgramState getData() {
        return getData(System.in);
    }

    public ProgramState putData(OutputStream outputStream) {
        try {
            outputStream.write(buf[ptr]);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ProgramState putData() {
        return putData(System.out);
    }

    public ProgramState startLoop() {
        if (buf[ptr] == 0) programPtr = program.getClosing(programPtr);
        return this;
    }

    public ProgramState stopLoop() {
        if (buf[ptr] != 0) programPtr = program.getOpening(programPtr);
        return this;
    }

    public ProgramState incrementProgramCounter() {
        programPtr++;
        return this;
    }

    public Program.Instruction currentInstruction() {
        if (programPtr < 0 || programPtr >= program.getLength()) return null;
        else return program.getInstruction(programPtr);
    }
}
