package gitlet;

import java.io.Serializable;

/**
 * TODO[Completed on 2025-05-10](QingZhiLiangCheng): add Pointer class.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */

public class Pointer implements Serializable {
    public String next;

    public Pointer(String id) {
        next = id;
    }
}
