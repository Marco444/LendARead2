package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.itba.edu.paw.persistenceinterfaces.ImagesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImagesDao imagesDao;


    @Override
    public byte[] getPhoto(int id) {
        Optional<byte[]> image = imagesDao.getPhoto(id);
        return image.get();
    }
}