import java.util.Random;

public class Tabuleiro {
    private static final char NAVIO_SUBMARINO = 'S';
    private static final char NAVIO_CRUZADOR = 'C';
    private static final char NAVIO_PORTA_AVIOES = 'P';
    private static final char AGUA = '~';
    private static final char TIRO_ACERTADO = 'X';
    private static final char TIRO_ERRADO = 'O';
    public static final int TAMANHO_TABULEIRO = 10;

    private char[][] tabuleiro;

    public Tabuleiro() {
        tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        preencherTabuleiro();
        posicionarNavios();
    }

    private void preencherTabuleiro() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiro[i][j] = AGUA;
            }
        }
    }

    private void posicionarNavios() {
        posicionarNaviosAleatoriamente(NAVIO_SUBMARINO, 3, 1);
        posicionarNaviosAleatoriamente(NAVIO_CRUZADOR, 2, 2);
        posicionarNaviosAleatoriamente(NAVIO_PORTA_AVIOES, 1, 3);
    }

    private void posicionarNaviosAleatoriamente(char navio, int quantidade, int tamanho) {
        Random random = new Random();
        for (int q = 0; q < quantidade; q++) {
            boolean posicaoValida;
            int x, y, direcao;

            do {
                x = random.nextInt(TAMANHO_TABULEIRO);
                y = random.nextInt(TAMANHO_TABULEIRO);
                direcao = random.nextInt(2);
                posicaoValida = verificarPosicaoValida(x, y, direcao, tamanho);
            } while (!posicaoValida);

            for (int t = 0; t < tamanho; t++) {
                if (direcao == 0) { // horizontal
                    tabuleiro[x][y + t] = navio;
                } else { // vertical
                    tabuleiro[x + t][y] = navio;
                }
            }
        }
    }

    private boolean verificarPosicaoValida(int x, int y, int direcao, int tamanho) {
        for (int t = 0; t < tamanho; t++) {
            if (direcao == 0) { // horizontal
                if (y + t >= TAMANHO_TABULEIRO || tabuleiro[x][y + t] != AGUA) {
                    return false;
                }
            } else { // vertical
                if (x + t >= TAMANHO_TABULEIRO || tabuleiro[x + t][y] != AGUA) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getTabuleiro() {
        return tabuleiro;
    }

    public boolean realizarTiro(int x, int y) {
        char alvo = tabuleiro[x][y];

        if (alvo == AGUA) {
            tabuleiro[x][y] = TIRO_ERRADO;
            return false;
        } else if (alvo != TIRO_ACERTADO && alvo != TIRO_ERRADO) {
            tabuleiro[x][y] = TIRO_ACERTADO;
            return true;
        }
        return false;
    }

    public boolean verificarFimDeJogo() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                char posicao = tabuleiro[i][j];
                if (posicao == NAVIO_SUBMARINO || posicao == NAVIO_CRUZADOR || posicao == NAVIO_PORTA_AVIOES) {
                    return false;
                }
            }
        }
        return true;
    }
}