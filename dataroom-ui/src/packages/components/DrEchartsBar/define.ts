import { ChartPlugin } from '@/packages/components/type/define.ts'
import thumbnail from './images/柱状图.png'

export default class DrEchartsBarPlugin extends ChartPlugin {
  constructor(tags: string[]) {
    super('DrEchartsBar', '柱状图', thumbnail, tags)
  }
}
