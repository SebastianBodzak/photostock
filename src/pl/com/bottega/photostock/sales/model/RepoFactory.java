package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.photostock.sales.infrastructure.repositories.*;

/**
 * Created by Dell on 2016-05-24.
 *
 * The class where you can switch repositories and all necessary data.
 * It contains methods creating fake repositories for tests.
 */
public class RepoFactory {

    private static final String pathToFileClientRepository = "tmp/clients.csv";
    private static final String pathToFileProductRepository = "tmp/products.csv";
    private static final String pathToFilePurchaseRepository = "tmp/purchases.csv";
    private static final String pathToFileReservationRepository = "tmp/reservations.csv";
    private static final String pathToFileLightBoxRepository = "tmp/lightboxes.csv";

    public static ClientRepository createClientRepository() {
        return new FileClientRepository(pathToFileClientRepository);
    }

    public static ProductRepository createProductRepository() {
        return new FileProductRepository(pathToFileProductRepository);
    }

    public static LightBoxRepository createLightBoxRepository() {
        return new FileLightBoxRepository(pathToFileLightBoxRepository, createClientRepository(), createProductRepository());
    }

    public static ReservationRepository createReservationRepository() {
        return new FileReservationRepository(pathToFileReservationRepository, createClientRepository(), createProductRepository());
    }

    public static PurchaseRepository createPurchaseRepository() {
        return new FilePurchaseRepository(pathToFilePurchaseRepository, createClientRepository(),createProductRepository());
    }

    /**
     * for test
     * @return
     */
    public static ClientRepository createFakeClientRepository() {
        return new FakeClientRepository();
    }

    /**
     * for test
     * @return
     */
    public static ProductRepository createFakeProductRepository() {
        return new FakeProductRepository();
    }

    /**
     * for test
     * @return
     */
    public static LightBoxRepository createFakeLightBoxRepository() {
        return new FakeLightBoxRepository();
    }

    /**
     * for test
     * @return
     */
    public static ReservationRepository createFakeReservationRepository() {
        return new FakeReservationRepository();
    }

    /**
     * for test
     * @return
     */
    public static PurchaseRepository createFakePurchaseRepository() {
        return new FakePurchaseRepository();
    }
}