/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tasca_3;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.Random;

/**
 *
 * @author Jumi
 */
public class MyBufferedImage extends BufferedImage {

    private static final int UNFOCUS_KERNEL[][] = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private static final int UNFOCUS_GAUSSIAN_KERNEL[][] = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    private static final int SHARP_KERNEL[][] = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    private static final int EDGES_KERNEL[][] = {{1, 1, 1}, {1, -9, 1}, {1, 1, 1}};
    private static final int FIRE_KERNEL[][] = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};

    byte[] imgOriginal;
    int cont = 0;
    int[][] colores;
    byte[] baOriginal;
    boolean gray;
    boolean squareGray;
    Rectangle square;
    String name;
    int bright;
    int blueBright;
    int greenBright;
    int pixelLength;
    int redBright;
    int squareBright;
    int squareRedBright;
    int squareGreenBright;
    int squareBlueBright;
    int focusLevel;
    int squareFocusLevel;
    int contrast;

    // Consructors -------------------------------------------------------------
    public MyBufferedImage(BufferedImage bi, String n) {
        super(
                bi.getColorModel(), bi.getRaster(),
                bi.getColorModel().isAlphaPremultiplied(), null);

        this.baOriginal = this.copyDataRasterToByteArray(bi.getRaster());
        this.resetBright();
        this.resetSquareBright();
        this.resetFocusLevel();

        this.gray = false;
        this.name = n;
        this.square = new Rectangle(0, 0, 0, 0);
        this.colores = new int[this.getHeight()][this.getWidth()];

        System.out.println(getType());

        if (this.getAlphaRaster() != null) {
            this.pixelLength = 4;
        } else {
            this.pixelLength = 3;
        }
    }

    // Publics -----------------------------------------------------------------
    public int getBright() {
        return this.bright;
    }

    public int getBlueBright() {
        return this.blueBright;
    }

    public int getRedBright() {
        return this.redBright;
    }

    public int getFocusLevel() {
        return this.focusLevel;
    }

    public boolean getGray() {
        return this.gray;
    }

    public int getGreenBright() {
        return this.greenBright;
    }

    public String getImageName() {
        return this.name;
    }

    public int getSquareBright() {
        return this.squareBright;
    }

    public int getSquareBlueBright() {
        return this.squareBlueBright;
    }

    public int getSquareFocusLevel() {
        return this.squareFocusLevel;
    }

    public boolean getSquareGray() {
        return this.squareGray;
    }

    public int getSquareGreenBright() {
        return this.squareGreenBright;
    }

    public int getSquareRedBright() {
        return this.squareRedBright;
    }

    public float getSquareSize() {
        float percent;

        percent = (float) this.square.width / (float) this.getWidth();
        return percent;
    }

    public boolean setBright(int brightLevel) {
        if (this.bright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.bright = brightLevel;
        this.processImage();

        return true;
    }

    public boolean setBlueBright(int brightLevel) {
        if (this.blueBright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.blueBright = brightLevel;
        this.processImage();

        return true;
    }

    public boolean setFocusLevel(int level) {
        if ((level >= 0) || (this.focusLevel == level)) {
            System.out.println("Nada que hacer");
            return false; // ================= Nada que hacer =================>
        }

        this.focusLevel = level;
        this.processImage();

        return true;
    }

    public boolean setGray(boolean g) {
        if (this.gray == g) {
            return false; // ================ Nada que hacer ==================>
        }

        this.gray = g;
        this.processImage();

        return true;
    }

    public boolean setGreenBright(int brightLevel) {
        if (this.greenBright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.greenBright = brightLevel;
        this.processImage();

        return true;
    }

    public boolean setRedBright(int brightLevel) {
        if (this.redBright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.redBright = brightLevel;
        this.processImage();

        return true;
    }

    public void setSquare(float percent) {
        float squareWidth, squareHeight;
        float xCentral, yCentral;
        int rIni, cIni;

        squareWidth = ((float) this.getWidth()) * percent / 100f;
        squareHeight = ((float) this.getHeight()) * percent / 100f;

        xCentral = this.getWidth() / 2;
        yCentral = this.getHeight() / 2;
        rIni = (int) (yCentral - squareHeight / 2);
        cIni = (int) (xCentral - squareWidth / 2);

        this.square.x = cIni;
        this.square.y = rIni;
        this.square.width = (int) squareWidth;
        this.square.height = (int) squareHeight;

        this.processImage();
    }

    public boolean setSquareBlueBright(int brightLevel) {
        if (this.squareBlueBright == brightLevel) {
            return false;  // =============== Nada que hacer ==================>
        }

        this.squareBlueBright = brightLevel;
        this.processSquare();

        return true;
    }

    public boolean setSquareBright(int brightLevel) {
        if (this.squareBright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.squareBright = brightLevel;
        this.processSquare();

        return true;
    }

    public boolean setSquareGray(boolean gray) {
        if (this.squareGray == gray) {
            return false; // =============== Nada que hacer ===================>
        }
        this.squareGray = gray;
        this.processSquare();

        return true;
    }

    public boolean setSquareGreenBright(int brightLevel) {
        if (this.squareGreenBright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.squareGreenBright = brightLevel;
        this.processSquare();

        return true;
    }

    public boolean setSquareFocusLevel(int level) {
        if (this.squareFocusLevel == level) {
            return false; // ================ Nada que hacer ==================>
        }

        this.squareFocusLevel = level;
        this.processSquare();

        return true;
    }

    public boolean setSquareRedBright(int brightLevel) {
        if (this.squareRedBright == brightLevel) {
            return false; // ================ Nada que hacer ==================>
        }

        this.squareRedBright = brightLevel;
        this.processSquare();

        return true;
    }

    public boolean setSquareUnfocusLevel(int level) {
        if ((level < 0) || (this.squareFocusLevel == level)) {
            return false; // ================ Nada que hacer ==================>
        }

        this.squareFocusLevel = level;
        this.processSquare();
        return true;
    }

    public boolean setUnfocusLevel(int level) {
        if ((level < 0) || (this.focusLevel == level)) {
            return false; // ================ Nada que hacer ==================>
        }

        // Aplicar el nivel de desenfoque al recuadro externo
        this.focusLevel = level;
        this.processImage();

        return true;
    }

    public void fire() {
        byte[] baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
        //hay que actualizar cont en el metodo limpiar!!
        if (this.cont == 0) {
            this.imgOriginal = baTarget;
        }
        this.cont++;
        byte[][] paleta = this.paletaFuego();
        this.filaInferior();
        int suma;
        int a = 0;
        for (int x = this.getHeight() - 2; x > 1; x--) {
            for (int y = 1; y < this.getWidth() - 1; y++) {
                suma = 0;
                for (int i = -1; i < 2; i++) {
                    suma += this.colores[x + 1][y + i];
                }
                suma += this.colores[x][y];
                this.colores[x][y] = suma / 4;
                if (a > 2) {
                    if (this.pixelLength == 4) {
                        if (this.colores[x][y] > 63) {
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength] = paleta[0][this.colores[x][y]];
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 2] = paleta[2][this.colores[x][y]];
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 3] = paleta[3][this.colores[x][y]];
                        } else {
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength] = this.baOriginal[((x + 4) * this.getWidth() + y) * this.pixelLength];
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 1] = this.baOriginal[((x + 4) * this.getWidth() + y) * this.pixelLength + 1];
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 2] = this.baOriginal[((x + 4) * this.getWidth() + y) * this.pixelLength + 2];
                            baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 3] = this.baOriginal[((x + 4) * this.getWidth() + y) * this.pixelLength + 3];
                        }
                    } else {
                        baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength] = paleta[0][suma / 4];
                        baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 1] = paleta[1][suma / 4];
                        baTarget[((x + 4) * this.getWidth() + y) * this.pixelLength + 2] = paleta[2][suma / 4];
                    }
                }
            }
            a++;
        }
    }

    public void limpiar() {
        byte[] baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
        for (int x = this.getHeight() - 1; x > 0; x--) {
            for (int y = 0; y < this.getWidth() - 1; y++) {
                if (this.pixelLength == 4) {
                    baTarget[(x * this.getWidth() + y) * this.pixelLength] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength];
                    baTarget[(x * this.getWidth() + y) * this.pixelLength + 1] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength + 1];
                    baTarget[(x * this.getWidth() + y) * this.pixelLength + 2] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength + 2];
                    baTarget[(x * this.getWidth() + y) * this.pixelLength + 3] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength + 3];
                } else {
                    baTarget[(x * this.getWidth() + y) * this.pixelLength] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength];
                    baTarget[(x * this.getWidth() + y) * this.pixelLength + 1] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength + 1];
                    baTarget[(x * this.getWidth() + y) * this.pixelLength + 2] = this.baOriginal[(x * this.getWidth() + y) * this.pixelLength + 2];
                }
                this.colores[x][y] = 0;
            }
        }
    }

    // Privates ----------------------------------------------------------------
    private void filaInferior() {
        Random r = new Random();
        for (int i = 0; i < this.getWidth(); i++) {
            if (r.nextInt(99) > 36) {
                this.colores[this.getHeight() - 1][i] = 0;
            } else {
                this.colores[this.getHeight() - 1][i] = 254;
            }
        }
    }

    private byte[][] paletaFuego() {
        byte[][] paletafuego = new byte[this.pixelLength][256];
        int azul = 0;
        int verde = 0;
        int rojo = 0;
        int transparencia = 0;
        for (int i = 0; i < 255; i++) {
            if (i > 63 && i < 95) {
                transparencia = 50;
                rojo += 8;
            }
            if (i > 94 && i < 127) {
                transparencia += 2;
                rojo = 255;
                verde += 4;
            }
            if (i > 126 && i < 158) {
                verde += 4;
            }
            if (i > 157) {
                verde = 255;
            }
            if (this.pixelLength == 4) {
                paletafuego[0][i] = (byte) transparencia;
                paletafuego[1][i] = (byte) azul;
                paletafuego[2][i] = (byte) verde;
                paletafuego[3][i] = (byte) rojo;
            } else {
                paletafuego[0][i] = (byte) azul;
                paletafuego[1][i] = (byte) verde;
                paletafuego[2][i] = (byte) rojo;
            }

        }
        return paletafuego;
    }

    private void applyBrigthAndGray(
            byte[] baSource, byte[] baTarget, int i,
            int redBright, int greenBright, int blueBright,
            boolean gray) {

        int newR, newG, newB;

        newB = Byte.toUnsignedInt(baSource[i]) + blueBright;
        newG = Byte.toUnsignedInt(baSource[i + 1]) + greenBright;
        newR = Byte.toUnsignedInt(baSource[i + 2]) + redBright;

        newB = (newB > 255 ? 255 : newB);
        newB = (newB < 0 ? 0 : newB);
        newG = (newG > 255 ? 255 : newG);
        newG = (newG < 0 ? 0 : newG);
        newR = (newR > 255 ? 255 : newR);
        newR = (newR < 0 ? 0 : newR);

        if (gray) {
            newR = newG = newB = (byte) ((newR + newG + newB) / this.pixelLength);
        }

        baTarget[i] = (byte) newB;
        baTarget[i + 1] = (byte) newG;
        baTarget[i + 2] = (byte) newR;
    }

    private void externalSquareAreaApplyFilter(
            byte[] baSource, byte[] baTarget,
            int[][] kernel, int divisor) {

        int rowFin, colFin;

        rowFin = this.getHeight() - 1;
        colFin = this.getWidth() - 1;

        for (int row = 1; row < rowFin; row++) {
            for (int col = 1; col < colFin; col++) {
                if (!this.square.contains(col, row)) {
                    this.applyKernelConvolution(baSource, baTarget, kernel, divisor, row, col);
                }
            }
        }
    }

    // Aplica un filtro a un area determinada de la imagen
    private void applyFilter(
            byte[] baSource, byte[] baTarget,
            int rowIni, int colIni, int height, int width,
            int[][] kernel, int divisor) {

        int rowFin, colFin;

        rowFin = rowIni + height;
        colFin = colIni + width;

        for (int row = rowIni; row < rowFin; row++) {
            for (int col = colIni; col < colFin; col++) {
                this.applyKernelConvolution(baSource, baTarget, kernel, divisor, row, col);
            }
        }
    }

    private void applyKernelConvolution(
            byte[] baSource, byte[] baTarget,
            int[][] kernel, int divisor,
            int row, int col) {
        int i, color, rowLenght;
        int iPixel[] = {0, 0, 0};
        int dif;

        rowLenght = this.getWidth() * this.pixelLength;
        for (int relativeRow = -1; relativeRow <= 1; relativeRow++) {
            for (int relativeCol = -1; relativeCol <= 1; relativeCol++) {

                for (int canal = 0; canal <= 2; canal++) {
                    i = (relativeRow + row) * rowLenght
                            + (relativeCol + col) * this.pixelLength;
                    color = Byte.toUnsignedInt(baSource[i + canal]);
                    iPixel[canal] += kernel[relativeRow + 1][relativeCol + 1] * color;
                }
            }
        }

        i = row * rowLenght + col * this.pixelLength;
        for (int canal = 0; canal <= 2; canal++) {
            iPixel[canal] = iPixel[canal] / divisor;

            iPixel[canal] = (iPixel[canal] > 255 ? 255 : iPixel[canal]);
            iPixel[canal] = (iPixel[canal] < 0 ? 0 : iPixel[canal]);

            dif = ((int) (baTarget[i + canal]) - iPixel[canal]);
            dif = (dif < 0 ? -1 * dif : dif);

            baTarget[i + canal] = (byte) (iPixel[canal]);

            // Aplicar solo a determinada franja de colores
            // Se podria poner una orquilla para futuros efectos
            //            if (iPixel[canal] >= 50 & iPixel[canal] <= 150) {
            //                baTarget[i + canal] = (byte) (iPixel[canal]);
            //            }
            // Aceptar solo pequeños cambios
            // Se podria poner una orquilla para futuros efectos
            //            if (dif < 10) {
            //                baTarget[i + canal] = (byte) (iPixel[canal]);
            //            }
        }
    }

    // Modifica el brillo para un área especificada de la imagen
    private void brightAndGray(
            byte[] baSource, byte[] baTarget,
            int rowIni, int colIni, int height, int width,
            int redBright, int greenBright, int blueBright,
            boolean gray) {

        int i, rowFin, colFin;

        rowFin = rowIni + height;
        colFin = colIni + width;

        for (int row = rowIni; row < rowFin; row++) {
            for (int col = colIni; col < colFin; col++) {

                i = row * this.getWidth() * this.pixelLength
                        + col * this.pixelLength;

                this.applyBrigthAndGray(baSource, baTarget, i,
                        redBright, greenBright, blueBright, gray);
            }
        }
    }

    // Modifica el brillo para la imagen completa 
    // teniendo en cuenta el brillo del recuadro central
    private void fullBrightAndGray() {
        int i, h, w;
        int redB, greenB, blueB;
        boolean grayActual;
        byte[] baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        h = this.getHeight();
        w = this.getWidth();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {

                i = row * this.getWidth() * this.pixelLength
                        + col * this.pixelLength;

                if (this.square.contains(col, row)) {
                    redB = this.squareBright + this.squareRedBright;
                    greenB = this.squareBright + this.squareGreenBright;
                    blueB = this.squareBright + this.squareBlueBright;
                    grayActual = this.squareGray;
                } else {
                    redB = this.bright + this.redBright;
                    greenB = this.bright + this.greenBright;
                    blueB = this.bright + this.blueBright;
                    grayActual = this.gray;
                }

                this.applyBrigthAndGray(this.baOriginal, baTarget, i,
                        redB, greenB, blueB, grayActual);
            }
        }
    }

    private int calcNormalizedDivisor(int[][] kernel) {
        int divisor;

        divisor = 0;
        for (int rr = 0; rr <= 2; rr++) {
            for (int cc = 0; cc <= 2; cc++) {
                divisor += kernel[rr][cc];
            }
        }

        return divisor;
    }

    private byte[] copyByteArray(byte[] baSource) {
        byte[] baCopy;

        if (baSource == null) {
            throw new IllegalArgumentException("Byte array is null");
        }

        baCopy = new byte[baSource.length];
        System.arraycopy(baSource, 0, baCopy, 0, baSource.length);

        return baCopy;
    }

    private byte[] copyDataRasterToByteArray(Raster ras) {
        byte[] baDataRasterSource;

        if (ras.getDataBuffer().getDataType() != DataBuffer.TYPE_BYTE) {
            throw new IllegalArgumentException("RGB data type is not BYTE");
        }

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        return this.copyByteArray(baDataRasterSource);
    }

    private void exernalUnfocus() {
        byte[] baSource;
        byte[] baTarget;
        int divisor;

        divisor = calcNormalizedDivisor(MyBufferedImage.UNFOCUS_KERNEL);
        baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        for (int actualLevel = 1; actualLevel <= this.focusLevel; actualLevel++) {
            baSource = this.copyByteArray(baTarget);
            this.externalSquareAreaApplyFilter(
                    baSource, baTarget,
                    MyBufferedImage.UNFOCUS_KERNEL, divisor);
        }
    }

    private void externalFocus() {
        byte[] baSource;
        byte[] baTarget;
        int divisor;

        divisor = calcNormalizedDivisor(MyBufferedImage.SHARP_KERNEL);
        baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        for (int actualLevel = this.focusLevel; actualLevel < 0; actualLevel++) {
            baSource = this.copyByteArray(baTarget);
            this.externalSquareAreaApplyFilter(
                    baSource, baTarget,
                    MyBufferedImage.SHARP_KERNEL, divisor);
        }
    }

    private void processImage() {
        this.fullBrightAndGray();
        this.exernalUnfocus();
        this.squareUnfocus();
        this.externalFocus();
        this.squareFocus();
    }

    private void processSquare() {
        this.squareBrightAndGray();
        this.squareUnfocus();
        this.squareFocus();
    }

    private void resetBright() {
        if (this.bright == 0 & this.redBright == 0
                & this.greenBright == 0 & this.blueBright == 0) {
            return; // ========= Brillo original -> Nada que hacer ============>
        }

        this.bright = 0;
        this.redBright = 0;
        this.greenBright = 0;
        this.blueBright = 0;
        this.fullBrightAndGray();
    }

    private void resetFocusLevel() {
        this.focusLevel = 0;
        this.squareFocusLevel = 0;
    }

    private void resetSquareBright() {
        if (this.squareBright == 0 & this.squareRedBright == 0
                & this.squareGreenBright == 0 & this.squareBlueBright == 0) {
            return; // ========== Brillo original: nada que hacer =============>
        }

        this.squareBright = 0;
        this.squareRedBright = 0;
        this.squareGreenBright = 0;
        this.squareBlueBright = 0;

        this.fullBrightAndGray();
    }

    private void squareBrightAndGray() {
        int redB, greenB, blueB;
        byte[] baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        redB = this.squareBright + this.squareRedBright;
        greenB = this.squareBright + this.squareGreenBright;
        blueB = this.squareBright + this.squareBlueBright;

        this.brightAndGray(this.baOriginal, baTarget,
                this.square.y, this.square.x,
                this.square.height, this.square.width,
                redB, greenB, blueB, this.squareGray);
    }

    private void squareFocus() {
        byte[] baSource;
        byte[] baTarget;
        int divisor;

        divisor = calcNormalizedDivisor(MyBufferedImage.SHARP_KERNEL);
        baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        for (int actualLevel = this.squareFocusLevel; actualLevel < 0; actualLevel++) {
            baSource = this.copyByteArray(baTarget);

            this.applyFilter(baSource, baTarget,
                    this.square.y, this.square.x,
                    this.square.height, this.square.width,
                    MyBufferedImage.SHARP_KERNEL, divisor);
        }
    }

    private void squareUnfocus() {
        byte[] baSource;
        byte[] baTarget;
        int divisor;

        divisor = calcNormalizedDivisor(MyBufferedImage.UNFOCUS_KERNEL);
        baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        for (int actualLevel = 1; actualLevel <= this.squareFocusLevel; actualLevel++) {
            baSource = this.copyByteArray(baTarget);

            this.applyFilter(baSource, baTarget,
                    this.square.y, this.square.x,
                    this.square.height, this.square.width,
                    MyBufferedImage.UNFOCUS_KERNEL, divisor);
        }
    }
}
