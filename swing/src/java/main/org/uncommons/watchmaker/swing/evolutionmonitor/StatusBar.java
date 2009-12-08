// ============================================================================
//   Copyright 2006-2009 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package org.uncommons.watchmaker.swing.evolutionmonitor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

/**
 * Status bar component for the evolution monitor.  Can also be used separately to
 * provide basic status information without having to use the full evolution monitor.  
 * @author Daniel Dyer
 */
public class StatusBar extends Box implements IslandEvolutionObserver<Object>
{
    private final JLabel generationsLabel = new JLabel("N/A", JLabel.RIGHT);
    private final JLabel timeLabel = new JLabel("N/A", JLabel.RIGHT);
    private final JLabel populationLabel = new JLabel("N/A", JLabel.RIGHT);
    private final JLabel elitismLabel = new JLabel("N/A", JLabel.RIGHT);
    private final JLabel iterationTypeLabel = new JLabel("Generations: ");

    private volatile int islandPopulationSize = -1;

    public StatusBar()
    {
        super(BoxLayout.X_AXIS);
        add(new JLabel("Population: "));
        add(populationLabel);
        add(createHorizontalStrut(20));
        add(new JLabel("Elitism: "));
        add(elitismLabel);
        add(createHorizontalStrut(20));
        add(iterationTypeLabel);
        add(generationsLabel);
        add(createHorizontalStrut(20));
        add(new JLabel("Elapsed Time: "));
        add(timeLabel);
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        // Set component names for easy look-up from tests.
        populationLabel.setName("Population");
        elitismLabel.setName("Elitism");
        generationsLabel.setName("Generations");
        timeLabel.setName("Time");
    }


    public void populationUpdate(final PopulationData<?> populationData)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                elitismLabel.setText(String.valueOf(populationData.getEliteCount()));
                generationsLabel.setText(String.valueOf(populationData.getGenerationNumber() + 1));
                timeLabel.setText(formatTime(populationData.getElapsedTime()));
                if (islandPopulationSize > 0)
                {
                    int islandCount = populationData.getPopulationSize() / islandPopulationSize;
                    populationLabel.setText(islandCount + "x" + islandPopulationSize);
                    iterationTypeLabel.setText("Epochs: ");
                }
                else
                {
                    populationLabel.setText(String.valueOf(populationData.getPopulationSize()));
                }
            }
        });
    }


    public void islandPopulationUpdate(int islandIndex,
                                       PopulationData<? extends Object> populationData)
    {
        islandPopulationSize = populationData.getPopulationSize();
    }


    private String formatTime(long time)
    {
        long seconds = time / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        StringBuilder buffer = new StringBuilder();
        if (hours < 10)
        {
            buffer.append('0');
        }
        buffer.append(hours);
        buffer.append(':');
        if (minutes < 10)
        {
            buffer.append('0');
        }
        buffer.append(minutes);
        buffer.append(':');
        if (seconds < 10)
        {
            buffer.append('0');
        }
        buffer.append(seconds);
        return buffer.toString();
    }
}
