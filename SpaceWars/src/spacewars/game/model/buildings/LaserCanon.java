package spacewars.game.model.buildings;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import spacewars.game.model.GameState;
import spacewars.game.model.Player;
import spacewars.game.model.PlayerElement;
import spacewars.game.model.Ship;
import spacewars.gamelib.GameTime;
import spacewars.gamelib.Vector;

@SuppressWarnings("serial")
public class LaserCanon extends Building
{
   protected static final String name        = "Laser";
   protected static final int[]  laserRanges = new int[] { 150, 250, 400 };
   protected static final int[]  laserPowers = new int[] { 3, 4, 5 };
   
   protected PlayerElement       attackTarget;
   
   public LaserCanon(final Vector position, final Player player)
   {
      super(position, 10, 100, player, 300);
   }
   
   @Override
   public String getName()
   {
      return name;
   }
   
   public int getLaserRange()
   {
      return laserRanges[level];
   }
   
   public int getLaserPower()
   {
      return laserPowers[level];
   }
   
   public PlayerElement getAttackTarget()
   {
      return attackTarget;
   }
   
   public boolean canLaser(Ship ship)
   {
      return position.distance(ship.getPosition()) < getLaserRange();
   }
   
   private PlayerElement chooseAttackTarget()
   {
      final Player enemy = getEnemy();
      final GameState gameState = getGameState();
      
      for (Ship ship : gameState.getShips())
      {
         // only attack enemy ships that are in range
         if (this.canLaser(ship) && ship.getPlayer() == enemy) { return ship; }
      }
      
      // no target
      return null;
   }
   
   @Override
   public void upgrade()
   {
      super.upgrade();
      this.power += 10;
   }
   
   @Override
   public void update(GameTime gameTime)
   {
      if (hasEnergy())
      {
         attackTarget = chooseAttackTarget();
         if (attackTarget != null)
         {
            // attack ship
            attackTarget.attack(getLaserPower());
         }
      }
   }
   
   @Override
   protected void renderBuilding(Graphics2D g)
   {
      if (!isPlaced())
      {
         // draw laser range
         g.setColor(isPlaceable() ? Color.WHITE : Color.RED);
         g.drawOval(position.x - getLaserRange(), position.y - getLaserRange(), 2 * getLaserRange(), 2 * getLaserRange());
      }
      
      super.renderBuilding(g);
      
      // draw icon
      final int BORDER = 3;
      g.setColor(Color.BLACK);
      g.fill(new Polygon(new int[] { position.x, position.x + radius - BORDER, position.x, position.x - radius + BORDER }, new int[] { position.y - radius + BORDER, position.y, position.y + radius - BORDER, position.y }, 4));
      
      // draw attack line
      if (attackTarget != null)
      {
         final int STROKE_WIDTH = 1;
         final float TRANSPARENCY = 0.5f;
         
         final Composite composite = g.getComposite();
         final Stroke stroke = g.getStroke();
         g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, TRANSPARENCY));
         g.setStroke(new BasicStroke(STROKE_WIDTH));
         g.setColor(getPlayer().getColor());
         
         final Vector p1 = getPosition();
         final Vector p2 = attackTarget.getPosition();
         final Line2D line = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
         g.draw(line);
         
         g.setComposite(composite);
         g.setStroke(stroke);
      }
   }
}
