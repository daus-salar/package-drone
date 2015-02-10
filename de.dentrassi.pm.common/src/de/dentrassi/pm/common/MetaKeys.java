/*******************************************************************************
 * Copyright (c) 2014, 2015 Jens Reimann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.dentrassi.osgi.converter.ConverterManager;

public final class MetaKeys
{
    private MetaKeys ()
    {
    }

    public static String getString ( final Map<MetaKey, String> metadata, final String ns, final String key )
    {
        if ( metadata == null )
        {
            return null;
        }
        return metadata.get ( new MetaKey ( ns, key ) );
    }

    public static <T> T bind ( final T data, final Map<MetaKey, String> metadata ) throws Exception
    {
        final ConverterManager converter = ConverterManager.create ();

        if ( data == null )
        {
            return null;
        }

        final List<Field> fields = new LinkedList<> ();
        findFields ( data.getClass (), fields );

        final Gson gson = null;

        for ( final Field field : fields )
        {
            final MetaKeyBinding mkb = field.getAnnotation ( MetaKeyBinding.class );
            final String stringValue = metadata.get ( new MetaKey ( mkb.namespace (), mkb.key () ) );

            final Object value;
            if ( stringValue == null )
            {
                value = null;
            }
            else
            {
                value = converter.convertTo ( stringValue, field.getType () );
            }

            setValue ( field, data, value );
        }

        return data;
    }

    protected static Gson createGson ()
    {
        Gson gson;
        final GsonBuilder builder = new GsonBuilder ();
        gson = builder.create ();
        return gson;
    }

    public static final Map<MetaKey, String> unbind ( final Object data ) throws Exception
    {
        final ConverterManager converter = ConverterManager.create ();

        if ( data == null )
        {
            return null;
        }

        final List<Field> fields = new LinkedList<> ();
        findFields ( data.getClass (), fields );

        final Map<MetaKey, String> result = new HashMap<> ( fields.size () );

        for ( final Field field : fields )
        {
            final MetaKeyBinding mkb = field.getAnnotation ( MetaKeyBinding.class );
            final Object value = getValue ( field, data );
            final String stringValue = converter.convertTo ( value, String.class );
            if ( ( stringValue == null || stringValue.isEmpty () ) && mkb.emptyAsNull () )
            {
                result.put ( new MetaKey ( mkb.namespace (), mkb.key () ), null );
            }
            else
            {
                result.put ( new MetaKey ( mkb.namespace (), mkb.key () ), stringValue );
            }
        }

        return result;
    }

    private static Object getValue ( final Field field, final Object data ) throws IllegalArgumentException, IllegalAccessException
    {
        if ( field.isAccessible () )
        {
            return field.get ( data );
        }
        else
        {
            field.setAccessible ( true );
            try
            {
                return field.get ( data );
            }
            finally
            {
                field.setAccessible ( false );
            }
        }
    }

    private static void setValue ( final Field field, final Object target, final Object value ) throws IllegalArgumentException, IllegalAccessException
    {
        if ( field.isAccessible () )
        {
            internalSetField ( field, target, value );
        }
        else
        {
            field.setAccessible ( true );
            try
            {
                internalSetField ( field, target, value );
            }
            finally
            {
                field.setAccessible ( false );
            }
        }
    }

    protected static void internalSetField ( final Field field, final Object target, final Object value ) throws IllegalAccessException
    {
        if ( value != null || !field.getType ().isPrimitive () )
        {
            field.set ( target, value );
        }
    }

    private static void findFields ( final Class<?> clazz, final List<Field> result ) throws SecurityException
    {
        if ( clazz == null )
        {
            return;
        }

        for ( final Field f : clazz.getDeclaredFields () )
        {
            if ( f.isAnnotationPresent ( MetaKeyBinding.class ) )
            {
                result.add ( f );
            }
        }

        findFields ( clazz.getSuperclass (), result );
    }
}
