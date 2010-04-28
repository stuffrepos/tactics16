package tactics16.datamanager;

import java.io.File;
import java.security.KeyStore.Builder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tactics16.animation.AnimationGroup;
import tactics16.animation.AnimationGroupBuilder;
import tactics16.animation.GameImage;
import tactics16.util.javabasic.StringUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class AnimationGroupJsonLoader extends AbstractJsonObjectLoader<AnimationGroup> {

    public AnimationGroupJsonLoader(JSONObject jsonObject, File file) {
        super(jsonObject, file);
    }

    @Override
    protected AnimationGroup fromJson() throws JSONException {
        final AnimationGroupBuilder builder = new AnimationGroupBuilder() {

            @Override
            public GameImage loadImage(String imageName) {
                GameImage image = AnimationGroupJsonLoader.this.loadImage(imageName);
                image.getCenter().setXY(
                        image.getImage().getWidth() / 2,
                        image.getImage().getHeight());
                return image;
            }
        };

        new JsonKeyIterator(getRootJson().getJSONObject("animations")) {

            @Override
            public void iterate(JSONObject jsonObject, String key) throws JSONException {
                loadSpriteAction(builder, key, jsonObject.getJSONObject(key));
            }
        };

        new JsonArrayIterator(getRootJson().optJSONArray("images")) {

            @Override
            public void iterate(JSONArray jsonArray, int index) throws JSONException {
                loadImage(builder, jsonArray.getJSONObject(index));
            }
        };

        return builder.build();
    }

    private void loadSpriteAction(final AnimationGroupBuilder builder, final String name, JSONObject jsonObject) throws JSONException {
        builder.setChangeFrameInterval(name, jsonObject.getInt("changeFrameInterval"));

        new JsonArrayIterator(jsonObject.getJSONArray("images")) {

            @Override
            public void iterate(JSONArray jsonArray, int index) throws JSONException {
                builder.addImage(name, jsonArray.getString(index));
            }
        };
    }

    private void loadImage(final AnimationGroupBuilder builder, JSONObject jsonObject) throws JSONException {
        final String centerX = StringUtil.parseString(jsonObject.optString("centerX"));
        final String centerY = StringUtil.parseString(jsonObject.optString("centerY"));
        final Double scale = StringUtil.parseDouble(jsonObject.optString("scale"));

        new JsonArrayIterator(jsonObject.getJSONArray("targets")) {

            @Override
            public void iterate(JSONArray jsonArray, int index) throws JSONException {
                if (centerX != null) {
                    builder.setImageCenterX(jsonArray.getString(index), centerX);
                }
                if (centerY != null) {
                    builder.setImageCenterY(jsonArray.getString(index), centerY);
                }
                if (scale != null) {
                    builder.setImageScale(jsonArray.getString(index),scale);
                }
            }
        };
    }
}
