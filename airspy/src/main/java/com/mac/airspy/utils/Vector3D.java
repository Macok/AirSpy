package com.mac.airspy.utils;

import java.io.Serializable;

/**
 * Copyright 2008 - 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Vector3D implements Serializable {

    private static final long serialVersionUID = -7026354578113311982L;

    private double x, y, z;

    public Vector3D(double value) {
        this(value, value, value);
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D vector3D) {
        this.x = vector3D.x;
        this.y = vector3D.y;
        this.z = vector3D.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void move(Vector3D vector3D) {
        this.x += vector3D.x;
        this.y += vector3D.y;
        this.z += vector3D.z;
    }

    public void move(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public double[] getCoords() {
        return (new double[] { x, y,z });
    }

    public boolean equals(Object o) {
        if (o instanceof Vector3D) {
            Vector3D p = (Vector3D) o;
            return p.x == x && p.y == y && p.z == z;
        }
        return false;
    }

    public int hashCode() {
        return (int) (x + y + z);
    }

    public Vector3D add(Vector3D other) {
        double x = this.x + other.x;
        double y = this.y + other.y;
        double z = this.z + other.z;
        return new Vector3D(x, y, z);
    }

    public Vector3D subtract(Vector3D other) {
        double x = this.x - other.x;
        double y = this.y - other.y;
        double z = this.z - other.z;
        return new Vector3D(x, y, z);
    }

    public Vector3D multiply(double value) {
        return new Vector3D(value * x, value * y, value * z);
    }

    public Vector3D crossProduct(Vector3D other) {
        double x = this.y * other.z - other.y * this.z;
        double y = this.z * other.x - other.z * this.x;
        double z = this.x * other.y - other.x * this.y;
        return new Vector3D(x, y, z);
    }

    public double dotProduct(Vector3D other) {
        return other.x * x + other.y * y + other.z * z;
    }

    public Vector3D normalize() {
        double magnitude = Math.sqrt(dotProduct(this));
        return new Vector3D(x / magnitude, y / magnitude, z / magnitude);
    }

    public double level() {
        return Math.sqrt(dotProduct(this));
    }

    public Vector3D modulate(Vector3D other) {
        double x = this.x * other.x;
        double y = this.y * other.y;
        double z = this.z * other.z;
        return new Vector3D(x, y, z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public String toString() {
        return (new StringBuffer("[Vector3D x:")).append(x).append(" y:")
                .append(y).append(" z:").append(z).append("]").toString();
    }

}