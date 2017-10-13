package pl.yalgrin.gremphics.io;

public enum ImageSaveParam {
    JPEG_COMPRESSION(1f);

    private Object defaultValue;

    ImageSaveParam(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
