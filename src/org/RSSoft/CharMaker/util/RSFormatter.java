/*
 * Copyright (C) 2017 Richard Schorrig.
 * richard.schorrig@web.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.RSSoft.CharMaker.util;

import java.sql.Date;
import java.sql.Time;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Format the output of the logger to a convenient readable output.
 * the format is:
 * date | time | milliseconds | level | class name | function | message
 * @author Richard
 */
class RSFormatter extends SimpleFormatter
{
  public final static String separator = " | ";
  @Override
  public String format(LogRecord record)
  {
    Date date = new Date(record.getMillis());
    Time time = new Time(record.getMillis());    
    long milliseconds = record.getMillis() % 1000;
    
    String text;
    text = date.toString();
    text += separator + time.toString();
    text += separator + String.format("%4d", milliseconds);
    text += separator + record.getLevel().toString();
    text += separator + record.getSourceClassName();
    text += separator + record.getSourceMethodName();
    text += separator + record.getMessage();
    text += "\n";
    
    return text;
  }
}
