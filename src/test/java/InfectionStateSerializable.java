import org.junit.jupiter.api.Test;
import org.lepigslayer.outbreakZ.Infection.InfectionState;
import org.reflections.serializers.JsonSerializer;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

public class InfectionStateSerializable {

    @Test
    public void testSerializable(){
        assertTrue(Serializable.class.isAssignableFrom(InfectionState.class));
    }
}
