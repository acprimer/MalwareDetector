/*
 * Copyright 2011, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib.Code.Format;

import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.Opcode;
import org.jf.dexlib.Code.TwoRegisterInstruction;
import org.jf.dexlib.DexFile;
import org.jf.dexlib.Item;
import org.jf.dexlib.Util.AnnotatedOutput;
import org.jf.dexlib.Util.NumberUtils;

public class Instruction52c extends InstructionWithJumboReference implements TwoRegisterInstruction {
    public static final InstructionFactory Factory = new Factory();
    private short regA;
    private short regB;

    public Instruction52c(Opcode opcode, int regA, int regB, Item referencedItem) {
        super(opcode, referencedItem);

        if (regA >= 1 << 16) {
            throw new RuntimeException("The register number must be less than v65536");
        }

        if (regB >= 1 << 16) {
            throw new RuntimeException("The register number must be less than v65536");
        }

        this.regA = (short)regA;
        this.regB = (short)regB;
    }

    private Instruction52c(DexFile dexFile, Opcode opcode, byte[] buffer, int bufferIndex) {
        super(dexFile, opcode, buffer, bufferIndex);

        this.regA = (short)NumberUtils.decodeUnsignedShort(buffer, bufferIndex + 6);
        this.regB = (short)NumberUtils.decodeUnsignedShort(buffer, bufferIndex + 8);
    }

    protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
        out.writeByte(0xFF);
        out.writeByte(opcode.value);
        out.writeInt(getReferencedItem().getIndex());
        out.writeShort(getRegisterA());
        out.writeShort(getRegisterB());
    }

    public Format getFormat() {
        return Format.Format52c;
    }

    public int getRegisterA() {
        return regA & 0xFFFF;
    }

    public int getRegisterB() {
        return regB & 0xFFFF;
    }

    private static class Factory implements InstructionFactory {
        public Instruction makeInstruction(DexFile dexFile, Opcode opcode, byte[] buffer, int bufferIndex) {
            return new Instruction52c(dexFile, opcode, buffer, bufferIndex);
        }
    }
}
