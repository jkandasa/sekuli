package com.redhat.qe.sekuli.common.remote;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;

import com.redhat.qe.sekuli.common.SekuliCommonUtils;
import com.redhat.qe.sekuli.common.exceptions.MissingMandatoryFieldsException;
import com.redhat.qe.sekuli.common.exceptions.TargetImageNotAvailableException;
import com.redhat.qe.sekuli.common.model.ImageBase64;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteScreen extends DesktopScreenRegion {
    private static final Logger _logger = Logger.getLogger(RemoteScreen.class.getName());
    private static final String ERROR_TARGET_NOT_AVAILABLE = "TargetName[{0}] not available in the repository."
            + " Add it before to use.";

    private final RemoteMouse mouse;
    private final HashMap<String, ImageTarget> imagesRepository = new HashMap<>();

    public RemoteScreen(RemoteMouse mouse) {
        this.mouse = mouse;
    }

    public int clearAllTargets() {
        int cleared = imagesRepository.size();
        imagesRepository.clear();
        _logger.log(Level.FINE, "About to clear " + cleared + " image(s)");
        return cleared;
    }

    public List<String> listTargets() {
        ArrayList<String> targets = new ArrayList<String>();
        for (String target : imagesRepository.keySet()) {
            targets.add(target);
        }
        return targets;
    }

    public int addTarget(ImageBase64 imageBase64) throws IOException {
        if (!imageBase64.isValid()) {
            throw new MissingMandatoryFieldsException("Missing mandatory fields. Check your parameters!");
        }
        ImageTarget imageTarget = new ImageTarget(SekuliCommonUtils.imageFromBase64(imageBase64.getBase64String()));
        imageTarget.setMinScore(imageBase64.getSimilarity());
        imagesRepository.put(imageBase64.getTargetName(), imageTarget);
        _logger.log(Level.FINE, "New image[" + imageBase64 + "] added. Number of images in repository:"
                + imagesRepository.size());
        return imagesRepository.size();
    }

    public String captureBase64() throws IOException {
        return captureBase64(null);
    }

    public String captureBase64(ScreenRegion region) throws IOException {
        if (region != null) {
            return SekuliCommonUtils.imageToBase64(region.capture());
        } else {
            return SekuliCommonUtils.imageToBase64(capture());
        }
    }

    private ImageTarget get(String targetName) {
        if (!imagesRepository.containsKey(targetName)) {
            throw new TargetImageNotAvailableException(MessageFormat.format(ERROR_TARGET_NOT_AVAILABLE, targetName));
        } else {
            return imagesRepository.get(targetName);
        }
    }

    public ScreenRegion find(String targetName) {
        return find(get(targetName));
    }

    public List<ScreenRegion> findAll(String targetName) {
        return findAll(get(targetName));
    }

    public void click(String targetName) {
        ScreenRegion region = find(targetName);
        if (region != null) {
            mouse.click(region);
        } else {
            throw new TargetImageNotAvailableException(MessageFormat.format(
                    "TargetImage{name:{0}, similarity:{1}} not available on the screen!",
                    targetName, String.valueOf(get(targetName).getMinScore())));
        }
    }

}
