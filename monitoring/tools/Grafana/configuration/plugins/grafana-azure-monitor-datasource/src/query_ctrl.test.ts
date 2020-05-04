jest.mock('./css/query_editor.css', () => {
  return {};
});

jest.mock('./monaco/kusto_monaco_editor');

import { AzureMonitorQueryCtrl } from '../src/query_ctrl';
import TemplateSrvStub from '../specs/lib/template_srv_stub';
import Q from 'q';

describe('AzureMonitorQueryCtrl', function() {
  let queryCtrl: any;

  beforeEach(function() {
    AzureMonitorQueryCtrl.prototype.panelCtrl = {
      events: { on: () => {} },
      panel: { scopedVars: [] },
    };
    queryCtrl = new AzureMonitorQueryCtrl({}, {}, new TemplateSrvStub());
    queryCtrl.datasource = { $q: Q, appInsightsDatasource: { isConfigured: () => false } };
  });

  describe('init query_ctrl variables', function() {
    it('should set default query type to Azure Monitor', function() {
      expect(queryCtrl.target.queryType).toBe('Azure Monitor');
    });

    it('should set default App Insights editor to be builder', function() {
      expect(queryCtrl.target.appInsights.rawQuery).toBe(false);
    });

    it('should set query parts to select', function() {
      expect(queryCtrl.target.azureMonitor.resourceGroup).toBe('select');
      expect(queryCtrl.target.azureMonitor.metricDefinition).toBe('select');
      expect(queryCtrl.target.azureMonitor.resourceName).toBe('select');
      expect(queryCtrl.target.azureMonitor.metricName).toBe('select');
      expect(queryCtrl.target.appInsights.groupBy).toBe('none');
    });
  });

  describe('when the query type is Azure Monitor', function() {
    describe('and getOptions for the Resource Group dropdown is called', function() {
      const response = [{ text: 'nodeapp', value: 'nodeapp' }, { text: 'otherapp', value: 'otherapp' }];

      beforeEach(function() {
        queryCtrl.datasource.getResourceGroups = function() {
          return this.$q.when(response);
        };
        queryCtrl.datasource.azureMonitorDatasource = {
          isConfigured: function() {
            return true;
          },
        };
      });

      it('should return a list of Resource Groups', function() {
        return queryCtrl.getResourceGroups('').then(result => {
          expect(result[0].text).toBe('nodeapp');
        });
      });
    });

    describe('when getOptions for the Metric Definition dropdown is called', function() {
      describe('and resource group has a value', function() {
        const response = [
          { text: 'Microsoft.Compute/virtualMachines', value: 'Microsoft.Compute/virtualMachines' },
          { text: 'Microsoft.Network/publicIPAddresses', value: 'Microsoft.Network/publicIPAddresses' },
        ];

        beforeEach(function() {
          queryCtrl.target.azureMonitor.resourceGroup = 'test';
          queryCtrl.datasource.getMetricDefinitions = function(query) {
            expect(query).toBe('test');
            return this.$q.when(response);
          };
        });

        it('should return a list of Metric Definitions', function() {
          return queryCtrl.getMetricDefinitions('').then(result => {
            expect(result[0].text).toBe('Microsoft.Compute/virtualMachines');
            expect(result[1].text).toBe('Microsoft.Network/publicIPAddresses');
          });
        });
      });

      describe('and resource group has no value', function() {
        beforeEach(function() {
          queryCtrl.target.azureMonitor.resourceGroup = 'select';
        });

        it('should return without making a call to datasource', function() {
          expect(queryCtrl.getMetricDefinitions('')).toBe(undefined);
        });
      });
    });

    describe('when getOptions for the ResourceNames dropdown is called', function() {
      describe('and resourceGroup and metricDefinition have values', function() {
        const response = [{ text: 'test1', value: 'test1' }, { text: 'test2', value: 'test2' }];

        beforeEach(function() {
          queryCtrl.target.azureMonitor.resourceGroup = 'test';
          queryCtrl.target.azureMonitor.metricDefinition = 'Microsoft.Compute/virtualMachines';
          queryCtrl.datasource.getResourceNames = function(resourceGroup, metricDefinition) {
            expect(resourceGroup).toBe('test');
            expect(metricDefinition).toBe('Microsoft.Compute/virtualMachines');
            return this.$q.when(response);
          };
        });

        it('should return a list of Resource Names', function() {
          return queryCtrl.getResourceNames('').then(result => {
            expect(result[0].text).toBe('test1');
            expect(result[1].text).toBe('test2');
          });
        });
      });

      describe('and resourceGroup and metricDefinition do not have values', function() {
        beforeEach(function() {
          queryCtrl.target.azureMonitor.resourceGroup = 'select';
          queryCtrl.target.azureMonitor.metricDefinition = 'select';
        });

        it('should return without making a call to datasource', function() {
          expect(queryCtrl.getResourceNames('')).toBe(undefined);
        });
      });
    });

    describe('when getOptions for the Metric Names dropdown is called', function() {
      describe('and resourceGroup, metricDefinition and resourceName have values', function() {
        const response = [{ text: 'metric1', value: 'metric1' }, { text: 'metric2', value: 'metric2' }];

        beforeEach(function() {
          queryCtrl.target.azureMonitor.resourceGroup = 'test';
          queryCtrl.target.azureMonitor.metricDefinition = 'Microsoft.Compute/virtualMachines';
          queryCtrl.target.azureMonitor.resourceName = 'test';
          queryCtrl.datasource.getMetricNames = function(resourceGroup, metricDefinition, resourceName) {
            expect(resourceGroup).toBe('test');
            expect(metricDefinition).toBe('Microsoft.Compute/virtualMachines');
            expect(resourceName).toBe('test');
            return this.$q.when(response);
          };
        });

        it('should return a list of Metric Names', function() {
          return queryCtrl.getMetricNames('').then(result => {
            expect(result[0].text).toBe('metric1');
            expect(result[1].text).toBe('metric2');
          });
        });
      });

      describe('and resourceGroup, metricDefinition and resourceName do not have values', function() {
        beforeEach(function() {
          queryCtrl.target.azureMonitor.resourceGroup = 'select';
          queryCtrl.target.azureMonitor.metricDefinition = 'select';
          queryCtrl.target.azureMonitor.resourceName = 'select';
        });

        it('should return without making a call to datasource', function() {
          expect(queryCtrl.getMetricNames('')).toBe(undefined);
        });
      });
    });

    describe('when onMetricNameChange is triggered for the Metric Names dropdown', function() {
      const response = {
        primaryAggType: 'Average',
        supportAggOptions: ['Average', 'Total'],
        supportedTimeGrains: ['PT1M', 'P1D'],
        dimensions: [],
      };

      beforeEach(function() {
        queryCtrl.target.azureMonitor.resourceGroup = 'test';
        queryCtrl.target.azureMonitor.metricDefinition = 'Microsoft.Compute/virtualMachines';
        queryCtrl.target.azureMonitor.resourceName = 'test';
        queryCtrl.target.azureMonitor.metricName = 'Percentage CPU';
        queryCtrl.datasource.getMetricMetadata = function(resourceGroup, metricDefinition, resourceName, metricName) {
          expect(resourceGroup).toBe('test');
          expect(metricDefinition).toBe('Microsoft.Compute/virtualMachines');
          expect(resourceName).toBe('test');
          expect(metricName).toBe('Percentage CPU');
          return this.$q.when(response);
        };
      });

      it('should set the options and default selected value for the Aggregations dropdown', function() {
        queryCtrl.onMetricNameChange().then(() => {
          expect(queryCtrl.target.azureMonitor.aggregation).toBe('Average');
          expect(queryCtrl.target.azureMonitor.aggOptions).toBe(['Average', 'Total']);
          expect(queryCtrl.target.azureMonitor.timeGrains).toBe(['PT1M', 'P1D']);
        });
      });
    });
  });

  describe('and query type is Application Insights', function() {
    describe('when getOptions for the Metric Names dropdown is called', function() {
      const response = [{ text: 'metric1', value: 'metric1' }, { text: 'metric2', value: 'metric2' }];

      beforeEach(function() {
        queryCtrl.datasource.appInsightsDatasource.isConfigured = () => true;
        queryCtrl.datasource.getAppInsightsMetricNames = function() {
          return this.$q.when(response);
        };
      });

      it('should return a list of Metric Names', function() {
        return queryCtrl.getAppInsightsMetricNames().then(result => {
          expect(result[0].text).toBe('metric1');
          expect(result[1].text).toBe('metric2');
        });
      });
    });

    describe('when getOptions for the GroupBy segments dropdown is called', function() {
      beforeEach(function() {
        queryCtrl.target.appInsights.groupByOptions = ['opt1', 'opt2'];
      });

      it('should return a list of GroupBy segments', function() {
        const result = queryCtrl.getAppInsightsGroupBySegments('');
        expect(result[0].text).toBe('opt1');
        expect(result[0].value).toBe('opt1');
        expect(result[1].text).toBe('opt2');
        expect(result[1].value).toBe('opt2');
      });
    });

    describe('when onAppInsightsMetricNameChange is triggered for the Metric Names dropdown', function() {
      const response = {
        primaryAggType: 'avg',
        supportedAggTypes: ['avg', 'sum'],
        supportedGroupBy: ['client/os', 'client/city'],
      };

      beforeEach(function() {
        queryCtrl.target.appInsights.metricName = 'requests/failed';
        queryCtrl.datasource.getAppInsightsMetricMetadata = function(metricName) {
          expect(metricName).toBe('requests/failed');
          return this.$q.when(response);
        };
      });

      it('should set the options and default selected value for the Aggregations dropdown', function() {
        return queryCtrl.onAppInsightsMetricNameChange().then(() => {
          expect(queryCtrl.target.appInsights.aggregation).toBe('avg');
          expect(queryCtrl.target.appInsights.aggOptions).toContain('avg');
          expect(queryCtrl.target.appInsights.aggOptions).toContain('sum');
          expect(queryCtrl.target.appInsights.groupByOptions).toContain('client/os');
          expect(queryCtrl.target.appInsights.groupByOptions).toContain('client/city');
        });
      });
    });
  });
});
