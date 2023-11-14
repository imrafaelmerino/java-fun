package jsonvalues.spec;


/**
 * A very fast and memory efficient class to encode and decode to and from BASE64 in full accordance with RFC
 * 2045.<br><br> On Windows XP sp1 with 1.4.2_04 and later ;), this encoder and decoder is about 10 times faster on
 * small arrays (10 - 1000 bytes) and 2-3 times as fast on larger arrays (10000 - 1000000 bytes) compared to
 * <code>sun.misc.Encoder()/Decoder()</code>.<br><br>
 * <p>
 * On byte arrays the encoder is about 20% faster than Jakarta Commons Base64 Codec for encode and about 50% faster for
 * decoding large arrays. This implementation is about twice as fast on very small arrays (&lt 30 bytes). If
 * source/destination is a <code>String</code> this version is about three times as fast due to the fact that the
 * Commons Codec result has to be recoded to a <code>String</code> from <code>byte[]</code>, which is very
 * expensive.<br><br>
 * <p>
 * This encode/decode algorithm doesn't create any temporary arrays as many other codecs do, it only allocates the
 * resulting array. This produces less garbage, and it is possible to handle arrays twice as large as algorithms that
 * create a temporary array. (E.g. Jakarta Commons Codec). It is unknown whether Sun's
 * <code>sun.misc.Encoder()/Decoder()</code> produce temporary arrays but since performance is quite low it probably
 * does.<br><br>
 * <p>
 * The encoder produces the same output as the Sun one except that the Sun's encoder appends a trailing line separator
 * if the last character isn't a pad. Unclear why but it only adds to the length and is probably a side effect. Both are
 * in conformance with RFC 2045 though.<br> Commons codec seem to always att a trailing line separator.<br><br>
 *
 * <b>Note!</b>
 * The encode/decode method pairs (types) come in three versions with the <b>exact</b> same algorithm and thus a lot of
 * code redundancy. This is to not create any temporary arrays for transcoding to/from different format types. The
 * methods not used can simply be commented out.<br><br>
 * <p>
 * There is also a "fast" version of all decode methods that works the same way as the normal ones, but har a few
 * demands on the decoded input. Normally though, these fast versions should be used if the source of the input is known,
 * and it hasn't been tampered with.<br><br>
 * <p>
 * If you find the code useful, or you find a bug, please send me a note at base64 @ miginfocom . com.
 * <p>
 * Licence (BSD): ==============
 * <p>
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (base64 @ miginfocom . com) All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the MiG InfoCom AB nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Mikael Grev Date: 2004-aug-02 Time: 11:31:11
 * @version 2.2
 */

abstract class Base64 {
    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final byte[] BA;

    static {
        BA = new byte[CA.length];
        for (int i = 0; i < CA.length; i++) {
            BA[i] = (byte) CA[i];
        }
    }

    static int encodeToBytes(byte[] sArr, byte[] dArr, final int start) {
        final int sLen = sArr.length;

        final int eLen = (sLen / 3) * 3;              // Length of even 24-bits.
        final int dLen = ((sLen - 1) / 3 + 1) << 2;   // Returned character count

        // Encode even 24-bits
        for (int s = 0, d = start; s < eLen; ) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (sArr[s++] & 0xff) << 16 | (sArr[s++] & 0xff) << 8 | (sArr[s++] & 0xff);

            // Encode the int into four chars
            dArr[d++] = BA[(i >>> 18) & 0x3f];
            dArr[d++] = BA[(i >>> 12) & 0x3f];
            dArr[d++] = BA[(i >>> 6) & 0x3f];
            dArr[d++] = BA[i & 0x3f];
        }

        // Pad and encode last bits if source isn't even 24 bits.
        int left = sLen - eLen; // 0 - 2.
        if (left > 0) {
            // Prepare the int
            int i = ((sArr[eLen] & 0xff) << 10) | (left == 2 ? ((sArr[sLen - 1] & 0xff) << 2) : 0);

            // Set last four chars
            dArr[start + dLen - 4] = BA[i >> 12];
            dArr[start + dLen - 3] = BA[(i >>> 6) & 0x3f];
            dArr[start + dLen - 2] = left == 2 ? BA[i & 0x3f] : (byte) '=';
            dArr[start + dLen - 1] = '=';
        }

        return dLen;
    }


}
