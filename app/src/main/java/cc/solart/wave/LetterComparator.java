package cc.solart.wave;

import com.wiserz.pbibi.bean.BrandInfoBean;

import java.util.Comparator;

/**
 * Created by jackie on 2017/9/9 13:50.
 * QQ : 971060378
 * Used as : LetterComparator
 */
public class LetterComparator implements Comparator<BrandInfoBean> {

    @Override
    public int compare(BrandInfoBean header, BrandInfoBean content) {
        if (header == null || content == null) {
            return 0;
        }

        String headerSortLetters = header.getAbbre().toUpperCase();
        String contentSortLetters = content.getAbbre().toUpperCase();
        if (headerSortLetters == null || contentSortLetters == null) {
            return 0;
        }
        return headerSortLetters.compareTo(contentSortLetters);
    }
}
