package ca.edtoaster.components;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Program {
    protected enum Instruction {
        INC_PTR('>') {
            @Override
            ProgramState next(ProgramState state) {
                return state.incrementPointer();
            }
        },
        DEC_PTR('<') {
            @Override
            ProgramState next(ProgramState state) {
                return state.decrementPointer();
            }
        },
        INC_DAT('+') {
            @Override
            ProgramState next(ProgramState state) {
                return state.incrementData();
            }
        },
        DEC_DAT('-') {
            @Override
            ProgramState next(ProgramState state) {
                return state.decrementData();
            }
        },
        PUT_DAT('.') {
            @Override
            ProgramState next(ProgramState state) {
                return state.putData();
            }
        },
        GET_DAT(',') {
            @Override
            ProgramState next(ProgramState state) {
                return state.getData();
            }
        },
        JMP_FOR('[') {
            @Override
            ProgramState next(ProgramState state) {
                return state.startLoop();
            }
        },
        JMP_BAK(']') {
            @Override
            ProgramState next(ProgramState state) {
                return state.stopLoop();
            }
        };

        private char value;
        private static final Map<Character, Instruction> lookup = new HashMap<>();

        static {
            for (Instruction instruction : Instruction.values())
                lookup.put(instruction.value, instruction);
        }

        Instruction(char value) {
            this.value = value;
        }

        abstract ProgramState next(ProgramState state);

        static Instruction get(Character instruction) {
            return lookup.getOrDefault(instruction, null);
        }
    }

    private Source source;
    private Instruction[] instructions;
    private boolean compiled;

    private Program(Source source) {
        this.source = source;
        this.compiled = false;
    }

    private Program compile() {
        this.instructions = source.stream()
                .map(Instruction::get)
                .toArray(Instruction[]::new);
        this.compiled = true;
        return this;
    }

    public void execute() {
        ProgramState state = ProgramState.defaultState(instructions);

        Instruction currentInstruction;

        while ((currentInstruction = state.currentInstruction()) != null) {
            currentInstruction.next(state);
            state.incrementProgramCounter();
        }
    }


    public static Program from(InputStream stream) throws IOException {
        Source src = Source.from(stream);
        return from(src);
    }

    public static Program from(String file) throws IOException {
        return from(new FileInputStream(file));
    }

    public static Program from(Source src) {
        return new Program(src).compile();
    }

}
