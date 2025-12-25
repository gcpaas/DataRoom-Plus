import { ChartDefine } from '@/packages/components/type/define.ts'
import thumbnail from './images/柱状图.png'

export default class DrEchartsBarPlugin extends ChartDefine {
  constructor(tags: string[]) {
    super('DrEchartsBar', '柱状图', thumbnail, tags)
  }
}
