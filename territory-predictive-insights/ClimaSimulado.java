public class ClimaSimulado {

    private boolean tempestade;

    public ClimaSimulado() {
        this.tempestade = false;
    }

    public boolean isTempestade() {
        return this.tempestade;
    }

    public void setTempestade(boolean tempestade) {
        this.tempestade = tempestade;
    }
}