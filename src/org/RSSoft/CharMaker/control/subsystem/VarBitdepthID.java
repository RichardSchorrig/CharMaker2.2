/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charmaker2.control.subsystem;

/**
 *
 * @author Richard
 */
public class VarBitdepthID {
  private final String identifier;
  private final int bits;
  
  public VarBitdepthID(String id, int bits)
  {
    this.identifier = id;
    this.bits = bits;
  }
  
  public int getDepth()
  {
    return this.bits;
  }
  
  public String getIdentifier()
  {
    return this.identifier;
  }
  
  @Override
  public String toString()
  {
    return this.identifier;
  }
}
