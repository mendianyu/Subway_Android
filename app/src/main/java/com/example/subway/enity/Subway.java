package com.example.subway.enity;

import androidx.annotation.NonNull;

public class Subway
{
    public int id;
    public String line_name;
    public String station_name;
    public String next_station;

    public String direction;

    public Subway()
    {
    }

    public Subway(String line_name, String station_name, String next_station, String direction)
    {
        this.line_name = line_name;
        this.station_name = station_name;
        this.next_station = next_station;
        this.direction = direction;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Subway{" + "id=" + id + ", line_name='" + line_name + '\'' + ", station_name='" + station_name + '\'' + ", next_station='" + next_station + '\'' + ", direction='" + direction + '\'' + '}';
    }
}