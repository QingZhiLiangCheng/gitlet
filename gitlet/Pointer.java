package gitlet;

import java.io.Serializable;

/**
 * Done[Completed on 2025-05-10](QingZhiLiangCheng): 搭建Pointer类框架.
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
