package zms.song.bore.common.base

import kotlinx.coroutines.Job
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

/**
 * @author: Listensong
 * Time: 19-10-23 上午8:55
 * Desc: zms.song.bore.common.base.CompositeJobTest
 */
@RunWith(PowerMockRunner::class)
class CompositeJobTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun add() {
        val compositeJob = CompositeJob()
        val beforeSize = compositeJob.size()
        compositeJob.add(Job())
        val afterSize = compositeJob.size()
        assertTrue(afterSize - beforeSize == 1)
    }

    @Test
    fun clear() {
        val compositeJob = CompositeJob()
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        assertTrue(compositeJob.size() > 0)
        compositeJob.clear()
        assertTrue(compositeJob.size() == 0)
    }

    @Test
    fun isDisposed() {

    }

    @Test
    fun dispose() {
        val compositeJob = CompositeJob()
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        assertTrue(compositeJob.size() > 0)
        compositeJob.dispose()
        assertTrue(compositeJob.size() == 0)
    }

    @Test
    fun remove() {
        val compositeJob = CompositeJob()
        val compositeSize = compositeJob.size()
        val job = Job()
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(job)
        assertTrue(compositeJob.size() - compositeSize == 3)
        compositeJob.remove(job)
        assertTrue(compositeJob.size() - compositeSize == 2)
    }

    @Test
    fun delete() {
        val compositeJob = CompositeJob()
        val compositeSize = compositeJob.size()
        val job = Job()
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(job)
        assertTrue(compositeJob.size() - compositeSize == 3)
        compositeJob.delete(job)
        assertTrue(compositeJob.size() - compositeSize == 2)
        //assertTrue(job.isActive)
    }

    @Test
    fun size() {
        val compositeJob = CompositeJob()
        val compositeSize = compositeJob.size()
        compositeJob.add(Job())
        compositeJob.add(Job())
        compositeJob.add(Job())
        assertTrue(compositeJob.size() - compositeSize == 3)
    }
}