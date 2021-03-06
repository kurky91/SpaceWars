package spacewars.game.model.buildings;

import spacewars.game.model.Player;
import spacewars.gamelib.Vector;

@SuppressWarnings("serial")
public class Relay extends Building
{
   protected static final String name = "Relay";
   
   public Relay(final Vector position, final Player player)
   {
      super(position, 5, 100, player, 200);
   }
   
   @Override
   public String getName()
   {
      return name;
   }
}
