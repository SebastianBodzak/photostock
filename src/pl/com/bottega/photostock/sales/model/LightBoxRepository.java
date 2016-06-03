package pl.com.bottega.photostock.sales.model;

/**
 * Created by Dell on 2016-04-20.
 */
public interface LightBoxRepository {
    public LightBox load(String nr);
    public void save(LightBox lightBox);
    public void remove(String nr);
}
