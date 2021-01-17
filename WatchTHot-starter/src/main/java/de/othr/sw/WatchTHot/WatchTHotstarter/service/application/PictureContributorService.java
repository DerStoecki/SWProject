package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IPictureContributorService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRetrogramService;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;

public class PictureContributorService implements IPictureContributorService {

    private IRetrogramService retrogramService;
    public PictureContributorService(IRetrogramService retrogramService){
        this.retrogramService = retrogramService;
    }

    /**
     * inspired by: https://www.baeldung.com/java-images
     * @param identifier identifier; usually from VisualizerService
     * @param statisticList usually from Visualizer Service : Most Recent Statistics
     * @return
     */
    @Override
    public boolean sendStatistic(StatisticIdentifier identifier, List<Statistic> statisticList) {
        String imagePath = "src/main/resources/static/applicationdata/picture/";
        String originalPicture  = "statisticImage.png";
        String completePath = imagePath+originalPicture;
        String identifierString = identifier.toString();
        try {
            StringJoiner stringJoiner = new StringJoiner("");
            stringJoiner.add("Your ").add(identifierString).add(" Statistic: ").add("\n");
            statisticList.forEach(statistic -> {
                       stringJoiner.add(statistic.getClient().getName()).add(": ");
                       stringJoiner.add(String.valueOf(statistic.getConsumptionSavePercent())).add("\n");
            });
            BufferedImage statisticImage = ImageIO.read(new File(completePath));
            Graphics2D graphics = (Graphics2D) statisticImage.getGraphics();
            graphics.setFont(new Font("TimesRoman", Font.BOLD, 80));
            int height = statisticImage.getHeight() / 3;
            for(String line : stringJoiner.toString().split("\n")) {
                graphics.drawString(line, statisticImage.getWidth() / 4, height += graphics.getFontMetrics().getHeight());
            }
            ImageIO.write(statisticImage, "png", new File(imagePath+"newPicture.png"));
            //https://www.baeldung.com/java-base64-image-string
            byte[] fileContent = FileUtils.readFileToByteArray(new File(imagePath+"newPicture.png"));
            //Encoded String for Retrogram
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            return this.retrogramService.sendPicture(encodedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



}
