/*
 * 2016 Royal Institute of Technology (KTH)
 *
 * LSelector is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.kth.app.sim;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import se.kth.app.CRDT.GSet;
import se.kth.app.CRDT.ORSet;
import se.kth.app.CRDT.TwoPSet;
import se.kth.sim.compatibility.SimNodeIdExtractor;
import se.kth.system.HostMngrComp;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.KompicsEvent;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.Operation;
import se.sics.kompics.simulator.adaptor.Operation1;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.IntegerUniformDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;
import se.sics.kompics.simulator.events.system.KillNodeEvent;
import se.sics.kompics.simulator.events.system.SetupEvent;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.kompics.simulator.network.identifier.IdentifierExtractor;
import se.sics.kompics.simulator.stochastic.events.StochasticKompicsSimulatorEvent;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapServerComp;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class ScenarioGenTask2 {


    static Operation<SetupEvent> systemSetupOp = new Operation<SetupEvent>() {
        @Override
        public SetupEvent generate() {
            return new SetupEvent() {
                @Override
                public IdentifierExtractor getIdentifierExtractor() {
                    return new SimNodeIdExtractor();
                }
            };
        }
    };

    static Operation<StartNodeEvent> startBootstrapServerOp = new Operation<StartNodeEvent>() {

        @Override
        public StartNodeEvent generate() {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    selfAdr = ScenarioSetup.bootstrapServer;
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return BootstrapServerComp.class;
                }

                @Override
                public BootstrapServerComp.Init getComponentInit() {
                    return new BootstrapServerComp.Init(selfAdr);
                }
            };
        }
    };

    static Operation1<StartNodeEvent, Integer> testOp = new Operation1<StartNodeEvent, Integer>() {
        @Override
        public StartNodeEvent generate(final Integer integer) {
            final KAddress selfAdr;
            final KAddress target;

            {
                String selfIp = "193.0.0.0";
                selfAdr = ScenarioSetup.getNodeAdr(selfIp,0);

                String targetIp = "193.0.0.2";
                target = ScenarioSetup.getNodeAdr(targetIp, integer);
            }
            return new StartNodeEvent() {
                @Override
                public KAddress getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return TestComponentTask2.class;
                }

                @Override
                public TestComponentTask2.Init getComponentInit() {
                    return new TestComponentTask2.Init(selfAdr, target, integer);
                }
            };
        }
    };

    static Operation1 killNodeOp = new Operation1<KillNodeEvent, Integer>() {
        @Override
        public KillNodeEvent generate(final Integer self) {
            return new KillNodeEvent() {
                KAddress selfAdr;

                {
                    selfAdr = ScenarioSetup.getNodeAdr("192.0.0." + self, self);
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public String toString() {
                    return "KillPonger<" + selfAdr.toString() + ">";
                }
            };
        }
    };

    static Operation1<StartNodeEvent, Integer> startNodeOp = new Operation1<StartNodeEvent, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId) {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    String nodeIp = "193.0.0." + nodeId;
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, nodeId);

                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return HostMngrComp.class;
                }

                @Override
                public HostMngrComp.Init getComponentInit() {
                    return new HostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId, ORSet.class.getName());
                }

                @Override
                public Map<String, Object> initConfigUpdate() {
                    Map<String, Object> nodeConfig = new HashMap<>();
                    nodeConfig.put("system.id", nodeId);
                    nodeConfig.put("system.seed", ScenarioSetup.getNodeSeed(nodeId));
                    nodeConfig.put("system.port", ScenarioSetup.appPort);
                    return nodeConfig;
                }
            };
        }
    };

    public static SimulationScenario simpleBoot() {


        final long seed1 = 32523, seed2 = 3346347;

        SimulationScenario scen = new SimulationScenario() {
            {
                StochasticProcess killNode = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, killNodeOp, new IntegerUniformDistribution(4,6, new Random(seed1)));
                    }
                };
                StochasticProcess systemSetup = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, systemSetupOp);
                    }
                };
                StochasticProcess startBootstrapServer = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, startBootstrapServerOp);
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(3, startNodeOp, new BasicIntSequentialDistribution(1));
                    }
                };

                StochasticProcess startPeersToKill = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(3, startNodeOp, new BasicIntSequentialDistribution(4));
                    }
                };

                final StochasticProcess reviveNode = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(1, startNodeOp, new BasicIntSequentialDistribution(5));
                    }
                };

                StochasticProcess startTest = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, testOp, new IntegerUniformDistribution(1,3, new Random(seed2)));
                    }
                };

                StochasticProcess startTestSET = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(2, testOp, new BasicIntSequentialDistribution(1));
                    }
                };
                StochasticProcess startTestSETagain = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, testOp, new BasicIntSequentialDistribution(1));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                //startPeersToKill.startAfterTerminationOf(1000, startPeers);

                //killNode.startAfterTerminationOf(1000, startPeersToKill);

                startTest.startAfterTerminationOf(1000, startPeers);

                //reviveNode.startAfterTerminationOf(5000, startTest);

                //startTestSET.startAfterTerminationOf(2000, startTest);
                //startTestSETagain.startAfterTerminationOf(2000, startTestSET);


                terminateAfterTerminationOf(1000*1000, startTest);
            }
        };

        return scen;
    }
}
